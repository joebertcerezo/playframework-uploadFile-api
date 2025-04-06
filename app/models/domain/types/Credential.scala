package models
package domain
package types

import play.api.libs.json.*
import play.api.data.*
import play.api.mvc.*
import play.api.data.format.Formats.*
import play.api.data.format.Formatter
import play.api.*

import JsonFormats.*

opaque type Credential = String
object Credential {
  def apply(value: String): Credential = value
  given Format[Credential] =
    opaqueFormat(_.validate[Credential], value => JsString(value.toString()))

  extension (c: Credential) {
    def value: String = c
  }
}

object JsonFormats {
  def opaqueFormat[T](
      apply: JsValue => JsResult[T],
      extract: T => JsValue
  ): Format[T] = new {
    def writes(t: T): JsValue             = extract(t)
    def reads(json: JsValue): JsResult[T] = apply(json)
  }
}
