package models
package auth

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

import play.api.mvc.*

import cats.data.EitherT

/**
 * Action builder that provides authentication and authorization
 */
class SecureAction[A](
    authService: AuthService,
    bodyParser: BodyParser[A]
)(implicit ec: ExecutionContext)
    extends ActionBuilder[AuthRequest, A] {

  override def parser: BodyParser[A]                        = bodyParser
  protected override def executionContext: ExecutionContext = ec

  override def invokeBlock[A](
      request: Request[A],
      block: AuthRequest[A] => Future[Result]
  ): Future[Result] = {
    // Your authentication logic using EitherT for proper error handling
    (for {
      token <- EitherT.fromOption[Future](
        extractToken(request),
        Unauthorized("Missing token")
      )
      user <- EitherT(authService.authenticate(token))
    } yield user).fold(
      err => err,
      user => block(new AuthRequest(user, request))
    )
  }

  private def extractToken[A](request: Request[A]): Option[String] =
    request.headers.get("Authorization").map(_.replaceFirst("Bearer ", ""))
}

// Auth request with authenticated user
class AuthRequest[A](val user: User, request: Request[A])
    extends WrappedRequest[A](request)
