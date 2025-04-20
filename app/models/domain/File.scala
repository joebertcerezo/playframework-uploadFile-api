package models
package domain

import java.util.UUID

import types.*

case class File(id: IdFile, name: String, path: String, idUser: Option[IdUser])

object File {
  def apply(name: String, path: String): File =
    new File(IdFile(UUID.randomUUID()), name, path, None)

  def unapply(
      f: File
  ): Option[(IdFile, String, String, Option[IdUser])] = Some(
    Tuple.fromProductTyped(f)
  )
}
