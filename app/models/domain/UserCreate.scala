package models
package domain

import play.api.libs.json.*

import types.*

case class UserCreate(
    name: String,
    email: EmailUser,
    username: Username,
    password: String
) {
  def toDomain: User = User(name, email, username, password)
}

object UserCreate {
  given Format[UserCreate] = Json.format[UserCreate]
  def unapply(
      u: UserCreate
  ): Option[(String, EmailUser, Username, String)] = Some(
    Tuple.fromProductTyped(u)
  )
}
