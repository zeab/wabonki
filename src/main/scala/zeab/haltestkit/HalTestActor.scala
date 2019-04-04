package zeab.haltestkit

//Imports
import java.util.UUID
import akka.actor.{Actor, Props}
import zeab.logging.Logging
import scala.reflect.runtime.{universe => ru}

//TODO Clean this entire thing up
class HalTestActor extends Actor with Logging{

  val testRunId: String = UUID.randomUUID().toString

  def receive: Receive = inactive

  //TODO Fix the type erasure here...
  def inactive: Receive = {
    case testList: List[String] =>
      val theTestList =
      testList
        .flatMap{test => getInstance(test)}
        .map{test =>
          context.actorOf(Props[HalMinionActor]) ! test
          test
        }
      context.become(active(theTestList.size))
  }

  def active(totalTestCases:Int, completedTestCases:List[CompletedTest] = List.empty): Receive = {
    case completedTest: CompletedTest =>
      val actualCompletedTest = completedTest.copy(testRunId = testRunId)
      log.info(actualCompletedTest.toString)
      val newTotalList = completedTestCases ++ List(actualCompletedTest)
      if (newTotalList.size == totalTestCases){
        val passing = newTotalList.filter(test => test.testResult.testResult)
        if (passing.size == totalTestCases) log.info("suite=success")
        else log.info("suite=fail")
        context.system.terminate()
      }
      else context.become(active(totalTestCases, completedTestCases ++ List(actualCompletedTest)))
  }

  def getInstance(test:String): List[RegisteredTest] ={
    val m = ru.runtimeMirror(getClass.getClassLoader)
    val clazz = m.staticClass(test)
    val cm = m.reflectClass(clazz)
    val constructor = clazz.primaryConstructor.asMethod
    val constructionMirror = cm.reflectConstructor(constructor)
    val instance = constructionMirror.apply()
    instance.asInstanceOf[HalTest].testList
  }

}
