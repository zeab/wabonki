package zeab.timestamp

//Imports
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId, ZoneOffset, ZonedDateTime}

trait Timestamp{

  val timestampFormatKey: String = "yyyy-MM-dd-HH.mm.ss.SSS"

  def now: String = DateTimeFormatter.ofPattern(timestampFormatKey).format(LocalDateTime.now())

  def nowGMT: String = DateTimeFormatter.ofPattern(timestampFormatKey).format(ZonedDateTime.of(LocalDateTime.now(), ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC))

}
