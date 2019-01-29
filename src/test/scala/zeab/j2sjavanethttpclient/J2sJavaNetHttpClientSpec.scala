package zeab.j2sjavanethttpclient

//Imports
import org.scalatest.FunSpec

class J2sJavaNetHttpClientSpec extends FunSpec{

  describe("J2S Java Net Http Client") {
    it("Should get a 200 response from google.com"){
      HttpClient.invokeHttpClientResponse("http://google.com") match {
        case Right(resp) =>
          resp.responseStatusCode match {
            case 200 => assert(true)
            case _ => assert(false)
          }
        case Left(ex) => assert(false)
      }
    }
  }

}
