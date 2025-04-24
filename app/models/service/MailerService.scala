package models
package service

import javax.inject.*

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.mailer.*

import cats.data.*
import cats.implicits.*

import domain.*

@Singleton
class MailerService @Inject() (
    protected val mailerClient: MailerClient
) {

  def create: EitherT[Future, ResponseError, ResponseSuccess] = {
    for {
      // _ <- "".asInstanceOf[EitherT[Future, ResponseError, ResponseSuccess]]
      // emailContent <- EitherT.right(
      //   Email(
      //     "Im the subject",
      //     "fromMe@gmail.com",
      //     Seq("hackdog195@gmail.com"),
      //     bodyText = Some(views.html.mail().body),
      //     bodyHtml = Some(views.html.mail().body)
      //   )
      // )
      result <- EitherT {
        val emailContent = Email(
          "Im the subject",
          "procerezo@gmail.com",
          Seq("hackdog195@gmail.com"),
          bodyText = Some(views.html.mail().body),
          bodyHtml = Some(views.html.mail().body)
        )
        mailerClient.send(emailContent)
        Future.successful(Right(MailerCreated))
      }
    } yield result
  }

}
