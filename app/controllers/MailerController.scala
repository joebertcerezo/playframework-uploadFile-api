package controllers

import javax.inject.*

import scala.concurrent.ExecutionContext.Implicits.global

import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController
import play.api.i18n.I18nSupport

import models.service.MailerService
import models.domain.*

@Singleton
class MailerController @Inject() (
    val cc: ControllerComponents,
    mailerService: MailerService
) extends AbstractController(cc)
    with I18nSupport {

  def create = Action.async { implicit request =>
    mailerService.create.fold(_.toResult, _.toResult)
  }
}
