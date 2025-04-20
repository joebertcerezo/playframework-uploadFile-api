import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.data.FormBinding.Implicits.formBinding
import play.api.data.*
import play.api.mvc.*
import play.api.mvc.Results.*
import play.api.i18n.*
import play.api.libs.json.*

import cats.data.*

import models.domain.*

package object controllers {
  object FormHandler {

    def execute[T, A](form: Form[T])(
        action: T => EitherT[Future, ResponseError, ResponseSuccess]
    )(using req: Request[A], mp: MessagesProvider): Future[Result] =
      form
        .bindFromRequest()
        .fold(
          error => Future(invalid(error)),
          value => action(value).fold(_.toResult, _.toResult)
        )

    def invalid[T](
        error: Form[T]
    )(using mp: MessagesProvider): Result =
      BadRequest(
        Json.obj(
          "code" -> s"INVALID_FORM",
          "data" -> error.errorsAsJson
        )
      )
  }
}
