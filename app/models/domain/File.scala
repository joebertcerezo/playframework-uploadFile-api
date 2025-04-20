package models
package domain

import java.util.UUID

import types.*

case class File(id: IdFile, name: String, path: String, idUser: IdUser)

object File {
  def apply(name: String, path: String, idUser: IdUser): File =
    new File(IdFile(UUID.randomUUID()), name, path, idUser)
  
  def unapply(
      f: File
  ): Option[(IdFile, String, String, IdUser)] = Some(Tuple.fromProductTyped(f))
}
