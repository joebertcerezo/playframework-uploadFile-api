package controllers

import javax.inject.*

import play.api.mvc.*
import play.api.i18n.I18nSupport

@Singleton
class FileController @Inject() (
    cc: ControllerComponents
) extends AbstractController(cc) with I18nSupport {
    
}
