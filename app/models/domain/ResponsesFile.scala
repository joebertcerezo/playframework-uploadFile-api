package models
package domain

import play.api.libs.json.*
import play.api.mvc.Results.*

def FileCreated = ResponseSuccess(
  "FILE_CREATED",
  Created
)

