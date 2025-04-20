package models
package domain

import play.api.libs.json.*

import models.domain.types.*

case class FileCreate(name: String, path: String, idUser: IdUser) {
  def toDomain: File = File(name, path, idUser)
}

object FileCreate {
  given Format[FileCreate] = Json.format[FileCreate]

  def unapply(f: FileCreate): Option[(String, String, IdUser)] = Some(
    Tuple.fromProductTyped(f)
  )
}
