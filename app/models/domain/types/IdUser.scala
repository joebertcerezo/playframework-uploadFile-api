package models
package domain
package types

import java.util.UUID

opaque type IdUser = UUID
object IdUser {
  def apply(value: IdUser)      = value
  def fromString(value: String) = UUID.fromString(value)

  extension (x: IdUser)
    def value: UUID      = x
    def toString: String = x.toString
}
