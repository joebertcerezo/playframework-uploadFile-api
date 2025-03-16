package models
package domain

import java.util.UUID

import play.api.libs.json.*
import play.api.mvc.Results.*

def UserCreated = ResponseSuccess(
  "USER_CREATED",
  Created
)
