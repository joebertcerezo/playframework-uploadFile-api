package models
package domain

import types.Credential

case class UserLogin(credential: Credential, password: String)

object UserLogin {
  def unapply(u: UserLogin): Option[(Credential, String)] = Some(
    Tuple.fromProductTyped(u)
  )
}