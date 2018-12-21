package zeab.seed.terminal

case class TerminalSeed(
                         command: String,
                         workingDir: Option[String] = None
                       )