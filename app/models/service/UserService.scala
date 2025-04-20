package models
package service

import javax.inject.*
import java.util.UUID

import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.*

import cats.data.*
import cats.implicits.*

import domain.*
import repo.UserRepo

@Singleton
class UserService @Inject() (
    val userRepo: UserRepo
) {
  def create(
      user: UserCreate
  ): EitherT[Future, ResponseError, ResponseSuccess] = {
    for {
      result <- EitherT {
        userRepo.Table.create(user.toDomain).map(_ => Right(UserCreated))
      }
    } yield result
  }
}
