package models
package domain
package types

import java.util.UUID

import play.api.libs.json.*
import play.api.data.format.Formatter

import JsonFormats.*

opaque type IdUser = UUID
object IdUser {
  def apply(value: UUID): IdUser = value

  given Format[IdUser] =
    opaqueFormat(_.validate[UUID], value => JsString(value.toString()))

  given Formatter[IdUser] = opaqueFormatter(
    value => IdUser(UUID.fromString(value)),
    _.toString
  )

  def fromString(value: String) = UUID.fromString(value)

  extension (x: IdUser)
    def value: UUID      = x
    def toString: String = x.toString
}
