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
  def create(user: User) = ???
}
