package models
package domain
package types

import java.util.UUID

import play.api.libs.json.*
import play.api.data.format.Formatter
import play.api.data.format.Formats.uuidFormat

import JsonFormats.*

opaque type IdUser = UUID
object IdUser {
  def apply(value: UUID): IdUser = value

  given Format[IdUser] =
    opaqueFormat(_.validate[UUID], value => JsString(value.toString()))

  given Formatter[IdUser] = opaqueFormatter[UUID, IdUser](
    apply = IdUser.apply, // UUID => IdUser
    extract = _.value     // IdUser => UUID
  )(using uuidFormat)

  def fromString(value: String) = UUID.fromString(value)

  extension (x: IdUser)
    def value: UUID      = x
    def toString: String = x.toString
}
