package models
package domain

import java.util.UUID

import play.api.libs.json.*

import types.*

case class User(
    id: IdUser,
    name: String,
    email: EmailUser,
    username: String,
    password: String,
    avatar: Option[String]
)

object User {
  given Format[User] = Json.format[User]

  def apply(
      name: String,
      email: EmailUser,
      username: String,
      password: String,
  ): User =
    new User(IdUser(UUID.randomUUID()), name, email, username, password, None)

  def unapply(
      u: User
  ): Option[(IdUser, String, EmailUser, String, String, Option[String])] = Some(
    Tuple.fromProductTyped(u)
  )
}
