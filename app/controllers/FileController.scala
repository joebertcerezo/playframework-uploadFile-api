package controllers

import javax.inject.*
import java.util.UUID
import java.nio.file.Paths
import java.nio.file.Files

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import play.api.*
import play.api.libs.json.*
import play.api.mvc.* // ControllerComponents
import play.api.i18n.I18nSupport
import play.api.data.Forms.* //form types
import play.api.data.Form    //form
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

import models.service.FileService
import models.auth.*
import models.domain.*
import models.domain.types.*

@Singleton
class FileController @Inject() (
    val cc: ControllerComponents,
    fileService: FileService,
    secureAction: SecureAction
) extends AbstractController(cc)
    with I18nSupport {

  def create: Action[MultipartFormData[TemporaryFile]] =
    Action.async(parse.multipartFormData) { implicit request =>
      request.body.file("file") match {
        case None =>
          Future.successful(
            BadRequest(
              Json.obj(
                "code" -> "INVALID_FORM",
                "data" -> Json.obj("file" -> "No file provided")
              )
            )
          )
        case Some(filePart) if !isValidImage(filePart) =>
          Future.successful(
            BadRequest(
              Json.obj(
                "code" -> "INVALID_FILE",
                "data" -> Json.obj(
                  "file" -> "Invalid file. Only image allowed, max 5MB."
                )
              )
            )
          )
        case Some(filePart) =>
          FormHandler.execute(FileForm.create)(fileService.create(_, filePart))
      }
    }

  private def isValidImage(image: FilePart[TemporaryFile]): Boolean = {
    val allowedTypes = Set("image/png", "image/jpeg")
    val maxSize      = 5 * 1024 * 1024 // 5MB
    image.contentType.exists(allowedTypes.contains) && image.fileSize <= maxSize
  }
}

object FileForm {
  val create = Form(
    mapping(
      "name" -> nonEmptyText,
      "path" -> optional(nonEmptyText)
    )(FileCreate.apply)(FileCreate.unapply)
  )
}
