package zeab

//Imports
import zeab.seed.terminal.{TerminalError, TerminalResponse}
//Scala
import scala.sys.process._
import scala.util.{Failure, Success, Try}

package object terminalclient {

  /** Execute a cmd command and return the result once its been completed */
  def exec(cmd: String, successCodes: Option[List[String]] = None): Either[TerminalError, TerminalResponse] = {
    //Determine the OS to provide the right type of terminal
    val cmdBundle = System.getProperty("os.name") match {
      case "Windows" => Seq("cmd", "-c", cmd)
      case "Linux" =>
        val getLinuxOS = "cat /etc/os-release".!!
        "NAME=.*".r.findFirstIn(getLinuxOS) match {
          case Some(os) =>
            os.replace("NAME=", "").replace('"', ' ').trim match {
              case "Ubuntu" => Seq("bash", "-c", cmd)
              case "Alpine Linux" => Seq("ash", "-c", cmd)
              case _ => Seq("bash", "-c", cmd)
            }
          case None => Seq("bash", "-c", cmd)
        }
    }
    //TODO Try and make this tread safe somehow since StringBuilder is not
    val stdout = new StringBuilder
    val stderr = new StringBuilder
    val execCmd = Try(cmdBundle ! ProcessLogger(stdout append _ + "\n", stderr append _ + "\n"), stdout, stderr)
    execCmd match {
      case Success(process) =>
        val (exitCode, stdout, stderr) = process
        if (exitCode == 0 || successCodes.getOrElse(List.empty).forall(code => exitCode.toString == code)) Right(TerminalResponse(stdout.toString, stderr.toString, Some(exitCode)))
        else Left(TerminalError(stdout.toString, stderr.toString, Some(exitCode)))
      case Failure(ex) => Left(TerminalError(stdout.toString, stderr.toString, Some(-1), Some(ex.toString)))
    }
  }

  /** Returns a stream of strings for the output of the command */
  def streamExec(cmd: String): Either[TerminalError, TerminalResponse] = {
    //Determine the OS to provide the right type of terminal
    val cmdBundle = System.getProperty("os.name") match {
      case "Windows" => Seq("cmd", "-c", cmd)
      case "Linux" => Seq("bash", "-c", cmd)
    }
    Try(cmdBundle.lineStream_!) match {
      case Success(stream) => Right(TerminalResponse(stdout.toString, stderr.toString, None, Some(stream)))
      case Failure(ex) => Left(TerminalError(stdout.toString, stderr.toString, Some(-1), Some(ex.toString)))
    }
  }

}
