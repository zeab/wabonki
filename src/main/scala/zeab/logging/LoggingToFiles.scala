package zeab.logging

//Imports
import ch.qos.logback.classic.Level
import org.slf4j.{Logger, LoggerFactory}

trait LoggingToFiles extends Logging{

  val fileLogLevel: Level = getLogLevel("FILE_LOG_LEVEL", "INFO")
  loggerContext.getLogger("file").setLevel(fileLogLevel)

  //Log to file
  val logFile: Logger = LoggerFactory.getLogger("file")

}
