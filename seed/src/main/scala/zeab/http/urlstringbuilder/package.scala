package zeab.http

package object urlstringbuilder {

  /** Format the override of the toString for just when you just have the host */
  private def hostFormat(schema: String, host: String): String = s"$schema://$host"

  /** Format the override of the toString for just when you just have the host and the port */
  private def portFormat(port: Option[String]): String = s"${if (port.isDefined) ":" else ""}${port.getOrElse("")}"

  /** Format the override of the toString for just when you have everything */
  private def hostParametersFormat(hostParameters: List[String]): String = hostParameters.mkString("/", "/", "")

  sealed trait UrlStringBuilder

  /** */
  case class Schema(schema: String) extends UrlStringBuilder {
    def host(host: String): Host = Host(schema, host)
  }

  /** */
  case class Host(schema: String, host: String) extends UrlStringBuilder {
    override def toString: String = hostFormat(schema, host)

    def port(port: String): Port = Port(schema, host, Some(port))

    def hostParameter(hostParameter: List[String]): HostParameter = HostParameter(schema, host, None, hostParameter)

    def hostParameter(value: String): HostParameter = HostParameter(schema, host, None, List(value))
  }

  /** */
  case class Port(schema: String, host: String, port: Option[String]) extends UrlStringBuilder {
    override def toString: String = s"${hostFormat(schema, host)}${portFormat(port)}"

    def hostParameter(hostParameter: List[String]): HostParameter = HostParameter(schema, host, port, hostParameter)

    def hostParameter(value: String): HostParameter = HostParameter(schema, host, port, List(value))
  }

  /** */
  case class HostParameter(schema: String, host: String, port: Option[String], hostParameter: List[String]) extends UrlStringBuilder {
    override def toString: String = s"${hostFormat(schema, host)}${portFormat(port)}${hostParametersFormat(hostParameter)}"

    def hostParameter(value: String): HostParameter = copy(schema, host, port, hostParameter ++ List(value))

    def queryParameter(queryParameter: Map[String, String]): QueryParameter = QueryParameter(schema, host, port, hostParameter, queryParameter)

    def queryParameter(key: String, value: String): QueryParameter = QueryParameter(schema, host, port, hostParameter, Map(key -> value))
  }

  /** */
  case class QueryParameter(schema: String, host: String, port: Option[String], hostParameter: List[String], queryParameter: Map[String, String]) extends UrlStringBuilder {
    override def toString: String = {
      def buildQuery(queryParameters: Map[String, String]): String = {
        def worker(queryParameters: List[(String, String)], query: String = ""): String = {
          queryParameters.size match {
            case 0 => s"?${query.drop(1)}"
            case _ =>
              val (queryKey, queryValue) = queryParameters.headOption.getOrElse(("", ""))
              worker(queryParameters.drop(1), s"$query&$queryKey=$queryValue")
          }
        }

        worker(queryParameters.toList)
      }

      if (queryParameter.isEmpty) s"${hostFormat(schema, host)}${portFormat(port)}${hostParametersFormat(hostParameter)}"
      else s"${hostFormat(schema, host)}${portFormat(port)}${hostParametersFormat(hostParameter)}${buildQuery(queryParameter)}"
    }

    def queryParameter(key: String, value: String): QueryParameter = copy(schema, host, port, hostParameter, queryParameter ++ Map(key -> value))
  }

  object UrlStringBuilder extends UrlStringBuilder {
    def schema(schema: String): Schema = Schema(schema)
  }

}
