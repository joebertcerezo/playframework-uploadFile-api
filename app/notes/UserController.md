package controllers

import javax.inject.*
import java.nio.file.Paths
import java.util.UUID
import java.nio.file.Files

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

import play.api.data.*
import play.api.data.Forms.*
import play.api.libs.json.*
import play.api.*
import play.api.mvc.*
import play.api.i18n.I18nSupport
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

import utilities.*

import models.domain.*
import models.service.UserService

@Singleton
class UserController @Inject() (
    cc: ControllerComponents,
    val secureAction: SecureAction,
    userService: UserService
)(using ExecutionContext)
    extends AbstractController(cc)
    with I18nSupport {

  def create = Action.async { implicit request =>
    FormHandler.execute(UserForm.create)(
      userService.create
    )
  }

  def upload: Action[MultipartFormData[TemporaryFile]] =
    Action.async(parse.multipartFormData) { implicit request =>
      request.body.file("image") match {
        case Some(image: FilePart[TemporaryFile]) =>
          // Validate file (e.g., size, type)
          if !isValidImage(image) then {
            Future.successful(
              BadRequest("Invalid image file. Only PNG/JPEG allowed, max 5MB.")
            )
          } else {
            // Generate a unique filename
            println(image.filename)
            println(image.fileSize)
            val fileExtension = image.filename
              .substring(image.filename.lastIndexOf("."))
              .toLowerCase
            val newFileName = s"${UUID.randomUUID()}$fileExtension"
            val uploadDir =
              Paths.get("uploads/avatars") // Ensure this directory exists
            val filePath = uploadDir.resolve(newFileName)

            // Create directory if it doesn’t exist
            Files.createDirectories(uploadDir)

            // Move the file to the target location
            image.ref.moveTo(filePath, replace = true)
            Future.successful(Ok("Image Uploaded"))
          }
        case None =>
          Future.successful(BadRequest("No image file provided"))
      }
    }

  private def isValidImage(image: FilePart[TemporaryFile]): Boolean = {
    val allowedTypes = Set("image/png", "image/jpeg")
    val maxSize      = 5 * 1024 * 1024 // 5MB
    image.contentType.exists(allowedTypes.contains) && image.fileSize <= maxSize
  }

  def update = secureAction.async { implicit request =>
    FormHandler.execute(UserForm.edit)(userService.update(request.idUser))
  }
}

object UserForm {

  val create = Form(
    mapping(
      "avatar"   -> optional(text(maxLength = 255)),
      "email"    -> of[EmailUser],
      "username" -> of[Username],
      "password" -> nonEmptyText(maxLength = 255),
      "avatar"   -> optional(text(maxLength = 255))
    )(User.apply)(User.unapply)
  )

  val edit = Form(
    mapping(
      "avatar"          -> optional(text(maxLength = 255)),
      "currentPassword" -> optional(text(maxLength = 255)),
      "newPassword"     -> optional(text(maxLength = 255)),
      "newName"         -> optional(text(maxLength = 255))
    )(UserUpdate.apply)(UserUpdate.unapply)
  )
}
