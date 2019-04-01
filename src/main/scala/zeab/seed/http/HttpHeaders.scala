package zeab.seed.http

trait HttpHeaders {
  val uniqueNameKey: String = "UniqueName"
  val acceptKey: String = "Accept"
  val dateKey: String = "Date"
  val digestKey: String = "Digest"
  val authorizationKey: String = "Authorization"
  val contentTypeKey: String = "Content-type"
}

object HttpHeaders extends HttpHeaders
