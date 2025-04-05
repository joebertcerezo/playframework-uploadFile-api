package controllers

import javax.inject.*

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.*
import play.api.mvc.* // ControllerComponents
import play.api.i18n.I18nSupport
import play.api.data.Forms.* //form types
import play.api.data.Form //form

import models.service.UserService
import models.auth.*
import models.domain.*

@Singleton
class UserController @Inject() (
    val cc: ControllerComponents,
    userService: UserService,
    secureAction: SecureAction
) extends AbstractController(cc)
    with I18nSupport {
  def create = Action.async { implicit request =>
    FormHandler.execute(UserForm.create)(userCreate => userService.create(userCreate))
  }
}

object UserForm {
  val create = Form(
    mapping(
      "name"            -> nonEmptyText,
      "email"           -> email,
      "password"        -> nonEmptyText(minLength = 6),
      "confirmPassword" -> nonEmptyText(minLength = 6)
    )(UserCreate.apply)(UserCreate.unapply)
  )
}
