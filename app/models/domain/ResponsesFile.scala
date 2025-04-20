package models
package domain

import play.api.libs.json.*
import play.api.mvc.Results.*

def FileCreated(data: JsValue) = ResponseSuccess(
  "FILE_CREATED",
  Created
)

def FileMoveFailed = ResponseError(
  "FILE_MOVE_FAILED",
  InternalServerError
)

def InvalidFile = ResponseError(
  "INVALID_FILE",
  BadRequest
)

def DirCreationFailed = ResponseError(
  "DIR_CREATION_FAILED",
  InternalServerError
)
