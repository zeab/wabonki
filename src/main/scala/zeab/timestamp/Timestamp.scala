package zeab.timestamp

//Imports
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZonedDateTime}

trait Timestamp {

  val timestampFormatKey: String = "yyyy-MM-dd-HH.mm.ss.SSS"
  val timestampFormat: SimpleDateFormat =  new java.text.SimpleDateFormat(timestampFormatKey)

  def now: String = timestampFormat.format(new java.util.Date())

  def nowGMT: String = DateTimeFormatter.ofPattern(timestampFormatKey).format(LocalDateTime.now().atZone(ZoneId.of("GMT")))

}
