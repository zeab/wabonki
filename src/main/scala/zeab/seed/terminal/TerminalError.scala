package zeab.seed.terminal

case class TerminalError(
                          stdout: String,
                          stderr: String,
                          exitCode: Option[Int] = None,
                          exception: Option[String] = None
                        )