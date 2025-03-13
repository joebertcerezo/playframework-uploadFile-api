package models
package domain

import play.api.libs.json.*
import play.api.mvc.Result
import play.api.mvc.Results.Status
import play.api.mvc.Session

private final case class Response(
    code: String,
    status: Status,
    data: JsValue = Json.obj(),
    session: Map[String, String] = Map.empty
) {
  def toResult: Result = session.isEmpty match {
    case true => status(Json.obj("code" -> code, "data" -> data))
    case false =>
      status(Json.obj("code" -> code, "data" -> data))
        .withSession(Session(data = session))
  }

  def withSession(x: Map[String, String]): Response = copy(session = x)
}

opaque type ResponseSuccess = Response
object ResponseSuccess {
  def apply(
      code: String,
      status: Status,
      data: JsValue = Json.obj(),
      session: Map[String, String] = Map.empty
  ): ResponseSuccess = Response(code, status, data, session)

  extension (x: ResponseSuccess)
    def toResult: Result = x.toResult
    def withSession(session: (String, String)*): ResponseSuccess =
      x.withSession(session.toMap)
}

opaque type ResponseError = Response
object ResponseError {
  def apply(
      code: String,
      status: Status,
      data: JsValue = Json.obj(),
      session: Map[String, String] = Map.empty
  ): ResponseError = Response(code, status, data, session)
  extension (x: ResponseError)
    def toResult: Result = x.toResult
    def withSession(session: (String, String)*): ResponseError =
      x.withSession(session.toMap)
}
