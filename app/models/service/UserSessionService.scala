package models
package service

import javax.inject.*

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import cats.data.*
import cats.implicits.*

import models.repo.UserRepo
import models.domain.*

@Singleton
class UserSessionService @Inject() (
    val userRepo: UserRepo
) {
  def create(
      user: UserLogin
  ): EitherT[Future, ResponseError, ResponseSuccess] = {
    for {
      _ <- OptionT(userRepo.Table.find(user.credential))
        .toRight(UserDoesNotExist)
      result <- "".asInstanceOf[EitherT[Future, ResponseError, ResponseSuccess]]
    } yield result
  }
}
