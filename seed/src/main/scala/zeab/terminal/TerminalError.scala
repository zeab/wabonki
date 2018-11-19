package zeab.terminal

case class TerminalError(
                          stdout: String,
                          stderr: String,
                          exitCode: Option[Int] = None,
                          exception: Option[String] = None
                        )