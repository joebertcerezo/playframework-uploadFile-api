package models
package domain
package types

opaque type Credential = String
object Credential {
  def apply(value: String): Credential = value
  extension (c: Credential) {
    def value: String = c
  }
}
