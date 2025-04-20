package models
package domain
package types

import java.util.UUID

import play.api.libs.json.*
import play.api.data.format.Formatter
import play.api.data.format.Formats.uuidFormat

import JsonFormats.*

opaque type IdFile = UUID
object IdFile {
  def apply(value: UUID): IdFile = value

  given Format[IdFile] =
    opaqueFormat(_.validate[UUID], value => JsString(value.toString()))

  given Formatter[IdFile] =
    opaqueFormatter(IdFile.apply, _.value)(using uuidFormat)

  extension (x: IdFile) {
    def value: UUID = x
  }
}
