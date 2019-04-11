package zeab.haltestkit.teststatuses

//Imports
import zeab.haltestkit.TestTag

case class RegisteredTest(
                           testName: String,
                           testTags: List[TestTag],
                           test: () => Any
                         )
