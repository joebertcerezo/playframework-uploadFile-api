package models
package domain

import java.util.UUID

import play.api.libs.json.Writes
import play.api.libs.json.Json
import play.api.libs.json.JsValue

import types.*

case class File(id: IdFile, name: String, path: String, idUser: Option[IdUser])

object File {
  given Writes[File] = (o: File) =>
    Json.obj(
      "name" -> o.name,
      "path" -> o.path
    )

  def apply(name: String, path: String): File =
    new File(IdFile(UUID.randomUUID()), name, path, None)

  def unapply(
      f: File
  ): Option[(IdFile, String, String, Option[IdUser])] = Some(
    Tuple.fromProductTyped(f)
  )
}
