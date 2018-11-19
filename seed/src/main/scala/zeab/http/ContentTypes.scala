package zeab.http

/** Http Content Types */
trait ContentTypes {
  /** JSON */
  val applicationJson: String = "application/json"
  /** XML */
  val applicationXml: String = "application/xml"
  /** Text Plain */
  val textPlain: String = "text/plain"
  /** HTML */
  val textHtml: String = "text/html"
}

/** Content Types */
object ContentTypes extends ContentTypes
