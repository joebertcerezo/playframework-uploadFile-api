package models
package domain

import java.util.UUID

import play.api.libs.json.*
import play.api.mvc.Results.*

def UserSessionValid(user: User) = ResponseSuccess(
  "USER_SESSION_VALID",
  Created,
  Json.toJson(user)
)

def UserLoggedin(id: UUID) = ResponseSuccess(
  "USER_LOGGEDIN",
  Created
).withSession("idUser" -> id.toString)

val UserLoggedout = ResponseSuccess(
  "USER_LOGGEDOUT",
  Ok
)

val IncorrectPassword = ResponseError(
  "USER_INCORRECT_CREDENTIALS",
  Unauthorized
)

val UnauthorizedAccess = ResponseError(
  "USER_UNAUTHORIZED_ACCESS",
  Unauthorized
)

val UserDoesNotExist = ResponseError(
  "USER_DOES_NOT_EXIST",
  NotFound
)

val VerificationFailed = ResponseError(
  "USER_VERIFICATION_FAILED",
  Unauthorized
)
