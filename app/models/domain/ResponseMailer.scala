package models
package domain

import play.api.libs.json.*
import play.api.mvc.Results.*

val MailerCreated = ResponseSuccess(
  "MESSAGE_SEND",
  Created
)
