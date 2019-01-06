package zeab.timestamp

import java.time.{DayOfWeek, LocalDateTime, ZoneId, ZonedDateTime}
//Circe
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._

object App extends Timestamp {

  def main(args: Array[String]): Unit = {


    case class llama(a:String, b:Int, c:ZonedDateTime)





    val rr = decode[llama]("""{"b":2,"a":"w"}""")

    println(rr.right.get.a)
    println(rr.right.get.b)
  }

}
