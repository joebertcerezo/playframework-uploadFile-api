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
}
