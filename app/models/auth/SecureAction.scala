package models
package auth

import java.util.UUID
import javax.inject.*

import scala.concurrent.ExecutionContext
import scala.concurrent.*

import play.api.mvc.*

import domain.*

class AuthRequest[A](idUser: UUID, request: Request[A])
    extends WrappedRequest[A](request)

class SecureAction @Inject() (
    val parser: BodyParsers.Default
)(using val executionContext: ExecutionContext)
    extends ActionBuilder[AuthRequest, AnyContent] {
  def invokeBlock[A](
      request: Request[A],
      block: AuthRequest[A] => Future[Result]
  ): Future[Result] = {
    val idKey = request.session.get("idUser")
    idKey match {
      case Some(id) => block(AuthRequest(UUID.fromString(id), request))
      case None     => Future(UnauthorizedAccess.toResult)
    }
  }
}
