package zeab.logging

//Imports
import zeab.envgrok.EnvGrok
//Logback
import ch.qos.logback.classic.{Logger => Logback}
import ch.qos.logback.classic.{Level, LoggerContext}
//Slf4j
import org.slf4j.{Logger, LoggerFactory}

trait Logging extends EnvGrok {

  val log: Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

  val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]

  val rootLogger: Logback = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
  val rootLogLevel: Level = envGrok("ROOT_LOG_LEVEL", "INFO").toUpperCase match {
    case "ERROR" => Level.ERROR
    case "WARN" | "WARNING" => Level.WARN
    case "INFO" => Level.INFO
    case "DEBUG" => Level.DEBUG
    case "OFF" => Level.OFF
  }
  rootLogger.setLevel(rootLogLevel)

}
