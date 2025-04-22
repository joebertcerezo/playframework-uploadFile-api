package controllers

import javax.inject.*

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.*
import play.api.mvc.* // ControllerComponents
import play.api.i18n.I18nSupport
import play.api.data.Forms.* //form types
import play.api.data.Form    //form

import models.service.UserService
import models.auth.*
import models.domain.*
import models.domain.types.*

@Singleton
class UserController @Inject() (
    val cc: ControllerComponents,
    userService: UserService,
    secureAction: SecureAction
) extends AbstractController(cc)
    with I18nSupport {
  def create = Action.async { implicit request =>
    FormHandler.execute(UserForm.create)(userService.create)
  }
}

object UserForm {
  val create = Form(
    mapping(
      "name"     -> nonEmptyText,
      "email"    -> of[EmailUser],
      "username" -> of[Username],
      "password" -> nonEmptyText(minLength = 6)
    )(UserCreate.apply)(UserCreate.unapply)
  )
}
