package models
package service

import javax.inject.*

import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

import cats.data.*
import cats.implicits.*

import domain.*
import models.repo.FileRepo

@Singleton
class FileService @Inject()(
  val fileRepo: FileRepo
){
  def create(
    file: FileCreate
  ): EitherT[Future, ResponseError, ResponseSuccess] = {
    for {
      
      result <- EitherT {
        val fileCreate = file.toDomain
        fileRepo.Table.create(fileCreate).map(_ => Right(FileCreated))
      }
    } yield result
  }
}

