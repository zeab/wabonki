package zeab.terminal

case class TerminalResponse(
                             stdout: String,
                             stderr: String,
                             exitCode: Option[Int] = None,
                             stream: Option[Stream[String]] = None
                           )