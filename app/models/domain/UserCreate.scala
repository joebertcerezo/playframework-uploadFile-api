package models
package domain

import play.api.libs.json.*

case class UserCreate(
    name: String,
    email: String,
    username: String,
    password: String
) {
  def toDomain: User = User(name, email, username, password)
}

object UserCreate {
  given Format[UserCreate] = Json.format[UserCreate]
  def unapply(
      u: UserCreate
  ): Option[(String, String, String, String)] = Some(
    Tuple.fromProductTyped(u)
  )
}
