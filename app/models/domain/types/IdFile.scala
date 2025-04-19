package models
package domain
package types

import java.util.UUID

import play.api.libs.json.*
import play.api.data.format.Formatter

import JsonFormats.*

opaque type IdFile = UUID
object IdFile {
  def apply(value: UUID): IdFile = value
}
