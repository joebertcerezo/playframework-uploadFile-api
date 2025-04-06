package controllers

import javax.inject.*

import play.api.mvc.*

@Singleton
class FileController @Inject() (
    cc: ControllerComponents
) extends AbstractController(cc)
