package models
package domain

import java.util.UUID

import types.*

case class File(id: UUID, name: String, path: String, idUser: IdUser)
