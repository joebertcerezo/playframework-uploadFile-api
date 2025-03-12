package models
package domain

import java.util.UUID

import play.api.libs.json.*

case class User(
    id: UUID,
    name: String,
    email: String,
    username: String,
    password: String,
    avatar: Option[String]
)

object User {
  given Format[User] = Json.format[User]

  def apply(
      name: String,
      email: String,
      username: String,
      password: String,
  ): User = new User(UUID.randomUUID(), name, email, username, password, None)

  def unapply(
      u: User
  ): Option[(UUID, String, String, String, String, Option[String])] = Some(
    Tuple.fromProductTyped(u)
  )
}
