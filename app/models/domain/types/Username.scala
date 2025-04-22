package models
package domain
package types

import play.api.libs.json.*
import play.api.data.format.Formatter
import play.api.data.format.Formats.stringFormat

import JsonFormats.*

opaque type Username = String
object Username {
  def apply(value: String): Username = value

  given Format[Username] =
    opaqueFormat(_.validate[String], value => JsString(value.toString()))

  given Formatter[Username] =
    opaqueFormatter(Username.apply, _.value)(using stringFormat)

  extension (x: Username) def value: String = x
}
