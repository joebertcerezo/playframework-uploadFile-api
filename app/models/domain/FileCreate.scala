package models
package domain

import java.util.UUID

import play.api.libs.json.*

import models.domain.types.*

case class FileCreate(
    name: String,
    path: Option[String]
) {
  def toDomain(path: String): File =
    File(name, path)
}

object FileCreate {
  given Format[FileCreate] = Json.format[FileCreate]

  def unapply(f: FileCreate): Option[(String, Option[String])] =
    Some(
      Tuple.fromProductTyped(f)
    )
}
