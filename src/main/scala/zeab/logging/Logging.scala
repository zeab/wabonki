package zeab.logging

//Imports
import zeab.envgrok.EnvGrok
//Other
import ch.qos.logback.classic.{Level, LoggerContext}
import org.slf4j.{Logger, LoggerFactory}

trait Logging extends EnvGrok{

  //Grab the logging context
  val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  //Set all the log levels
  val rootLogLevel: Level = getLogLevel("ROOT_LOG_LEVEL", "INFO")
  loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).setLevel(rootLogLevel)

  val akkaLogLevel: Level = getLogLevel("AKKA_LOG_LEVEL", "INFO")
  loggerContext.getLogger("akka").setLevel(akkaLogLevel)

  val kafkaLogLevel: Level = getLogLevel("KAFKA_LOG_LEVEL", "WARN")
  loggerContext.getLogger("org.apache.kafka").setLevel(kafkaLogLevel)

  //Log to console
  val log: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

  //Log to consume without json wrapping
  val logRaw: Logger = LoggerFactory.getLogger("stdout")

  def getLogLevel(key:String, default:String): Level ={
    envGrok(key, default).toUpperCase match {
      case "ERROR" => Level.ERROR
      case "WARN" | "WARNING" => Level.WARN
      case "INFO" => Level.INFO
      case "DEBUG" => Level.DEBUG
      case "OFF" => Level.OFF
      case "TRACE" => Level.TRACE
    }
  }

}