package models
package domain
package types

import play.api.libs.json.*
import play.api.data.format.Formatter
import play.api.data.format.Formats.stringFormat

import JsonFormats.*

opaque type Credential = String
object Credential {
  def apply(value: String): Credential = value

  given Format[Credential] =
    opaqueFormat(_.validate[Credential], value => JsString(value.toString()))

  given Formatter[Credential] =
    opaqueFormatter(Credential.apply, _.value)(using stringFormat)

  extension (c: Credential) {
    def value: String        = c
    def asEmail: EmailUser   = EmailUser(c)
    def asUsername: Username = Username(c)
  }
}
