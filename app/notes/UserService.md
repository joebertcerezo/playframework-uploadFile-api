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
import repo.*

@Singleton
class UserService @Inject() (
    val userRepo: UserRepo
) {
  def create(userCreate: UserCreate): EitherT[Future, ResponseError, ResponseSuccess] =
    for {
      result <- EitherT.right{
        val user = userCreate.toDomain()
        userRepo.Table.create(user).map(_ => UserCreated)
      }
    } yield result
    
  def create(user: User): EitherT[Future, ResponseError, ResponseSuccess] = {
    for {
      result <- EitherT {
        userRepo.Table.create(user).map(_ => Right(UserCreated))
      }
    } yield result
  }
}
