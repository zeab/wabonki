package zeab.haltestkit.halrunner

//Imports
import akka.routing.RoundRobinPool
import zeab.haltestkit.HalTest
import zeab.haltestkit.teststatuses.{CompletedTest, RegisteredTest}
//Java
import java.util.UUID
//Akka
import akka.actor.{Actor, ActorRef, Props}
//ClassUtil
import org.clapper.classutil.ClassFinder
//Scala
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.reflect.runtime.universe._

//TODO Make the entire thing safer... or apply the right supervisor strat ... or both...?

class HalRunner(dirForDiscovery: String, testTagsToRun: List[String], logger: ActorRef) extends Actor {

  implicit val ec: ExecutionContext = context.system.dispatcher

  val runTimeMirror: Mirror = runtimeMirror(getClass.getClassLoader)

  val testRunId: String = UUID.randomUUID.toString

  val igor: ActorRef = context.actorOf(RoundRobinPool(20).props(Props(classOf[HalRunnerMinion], self, testRunId)), "Igor")

  def receive: Receive = discovery

  def discovery: Receive = {
    case Discover =>
      val potentialTestClasses: List[(String, MethodMirror)] =
        ClassFinder()
          .getClasses
          .filter(_.isConcrete)
          .filter(name => name.name.contains(dirForDiscovery))
          .map { clazz => clazz.name }
          .map { testSuiteName =>
            val reflectedClassSymbol: ClassSymbol = runTimeMirror.staticClass(testSuiteName)
            val classMirror: ClassMirror = runTimeMirror.reflectClass(reflectedClassSymbol)
            val constructorMethod: MethodSymbol = reflectedClassSymbol.primaryConstructor.asMethod
            val constructorMirror: MethodMirror = classMirror.reflectConstructor(constructorMethod)
            testSuiteName -> constructorMirror
          }
          .toList
      self ! Register(potentialTestClasses)
    case m: Register =>
      val testClasses: List[(String, HalTest)] =
        m.potentialTestClasses.map { testClass =>
          val (testSuiteName, testFunction): (String, MethodMirror) = testClass
          val instance: HalTest = testFunction.apply(self).asInstanceOf[HalTest]
          testSuiteName -> instance
        }
      val totalNumberOfTests: Int = testClasses.unzip._2.map ( _.testCount ).sum
      context.become(testRegistry(totalNumberOfTests))
    case _ => logger ! "something else happened"
  }

  def testRegistry(expectedTestCount: Int, registeredTests: List[RegisteredTest] = List.empty): Receive = {
    case m: RegisteredTest =>
      val updatedRegisteredTests: List[RegisteredTest] = registeredTests ++ List(m)
      if (updatedRegisteredTests.size == expectedTestCount) {
        val filteredTests: List[RegisteredTest] =
          if (testTagsToRun.isEmpty) updatedRegisteredTests
          else
            updatedRegisteredTests.filter { test =>
              test.testTags.exists { tag =>
                test.testTags
                  .map (_.name)
                  .contains(tag.name)
              }
            }
        context.become(testRun(filteredTests))
        self ! StartTestRun
      }
      else context.become(testRegistry(expectedTestCount, updatedRegisteredTests))
    case _ => logger ! "Something else went wrong"
  }

  def testRun(registeredTests: List[RegisteredTest], completedTests: List[CompletedTest] = List.empty): Receive = {
    case StartTestRun =>
      //TODO Update this so that I tags for slow running test's so they get shuffled off into a blocking dispatcher
      registeredTests.foreach { test => igor ! test }
    case m: CompletedTest =>
      logger ! m
      val updatedCompletedTests: List[CompletedTest] = completedTests ++ List(m)
      if (registeredTests.size == updatedCompletedTests.size) {
        //reflect on the any class and find the first boolean and that will always be the test result value
        val testResults: List[Boolean] =
          updatedCompletedTests.map { test =>
            val typeName: String = runTimeMirror.reflect(test.testResult).symbol.name.toString
            typeName match {
              case "Boolean" => test.testResult.asInstanceOf[Boolean]
              case _ =>
                val reflectedResult: InstanceMirror = runTimeMirror.reflect(test.testResult)
                //Find the first instance of boolean and that is the test result ... as it is not the rule
                runTimeMirror.reflect(test.testResult).symbol.typeSignature.decls.find {
                  _.typeSignature.toString == "Boolean"
                } match {
                  case Some(sym) => reflectedResult.reflectField(sym.asTerm).get.asInstanceOf[Boolean]
                  case None => false
                }
            }
          }

        //TODO Decide if this is really where it belongs...
        testResults.find(!_) match {
          case Some(_) => logger ! "suite=failure"
          case None => logger ! "suite=success"
        }

        //Wait a few seconds and then close up shop
        context.system.scheduler.scheduleOnce(3.second) {
          context.system.terminate()
        }
      }
      else context.become(testRun(registeredTests, updatedCompletedTests))
  }

  case object StartTestRun

  case object Discover

  case object StartTestRegistry

  case class Register(potentialTestClasses: List[(String, MethodMirror)])

  //Lifecycle Hooks
  override def preStart: Unit = {
    self ! Discover
  }

  override def postStop: Unit = {
  }

}
