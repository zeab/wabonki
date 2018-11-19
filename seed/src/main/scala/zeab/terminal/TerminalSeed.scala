package zeab.terminal

case class TerminalSeed(
                         command: String,
                         workingDir: Option[String] = None
                       )