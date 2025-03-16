package controllers

import javax.inject.*

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.*
import play.api.mvc.* // ControllerComponents
import play.api.i18n.I18nSupport

import models.service.*

@Singleton
class UserController @Inject() (
    val cc: ControllerComponents,
    userService: UserService
) extends AbstractController(cc)
    with I18nSupport {}
