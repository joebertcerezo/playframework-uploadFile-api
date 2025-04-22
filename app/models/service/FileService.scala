package models
package service

import javax.inject.*
import java.util.UUID
import java.nio.file.Paths
import java.nio.file.Files

import scala.util.Try
import scala.util.Success
import scala.util.Failure
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.json.*
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart

import cats.data.*
import cats.implicits.*

import domain.*

import models.repo.FileRepo

@Singleton
class FileService @Inject() (
    val fileRepo: FileRepo
) {
  def create(
      fileCreate: FileCreate,
      filePart: FilePart[TemporaryFile]
  ): EitherT[Future, ResponseError, ResponseSuccess] = {
    for {
      // Validate file extension
      fileExtension <- EitherT.fromEither[Future] {
        val ext = filePart.filename
          .substring(filePart.filename.lastIndexOf("."))
          .toLowerCase
        if ext == ".jpeg" then Right(ext)
        else Left(InvalidFile)
      }

      // Generate unique filename and paths
      filePath <- EitherT.pure[Future, ResponseError] {
        val newFileName = s"${UUID.randomUUID()}$fileExtension"
        val uploadDir   = Paths.get("uploads/contracts")
        uploadDir.resolve(newFileName)
      }

      // Create directory
      _ <- EitherT {
        Future {
          Try(Files.createDirectories(filePath.getParent)).toEither.left.map(
            e => DirCreationFailed
          )
        }
      }

      // Move file
      _ <- EitherT {
        Future {
          Try(filePart.ref.moveTo(filePath, replace = true)).toEither.left.map(
            e => FileMoveFailed
          )
        }
      }

      // Create domain model
      file <- EitherT.pure[Future, ResponseError] {
        File(
          name = filePart.filename,
          path = filePath.toString
        )
      }

      // Persist to database
      result <- EitherT {
        val fileCreated = fileCreate.toDomain(filePath.toString())
        println(fileCreated)
        fileRepo.Table
          .create(fileCreated)
          .map(_ => Right(FileCreated(fileCreated)))
      }

      // Build response
      metadata = Json.obj(
        "id"   -> file.id,
        "name" -> file.name,
        "path" -> file.path
      )
    } yield result
  }
}
