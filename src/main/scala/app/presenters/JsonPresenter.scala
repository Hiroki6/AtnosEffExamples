package app.presenters

import cats.data.NonEmptyList
import cats.effect.IO
import app.commons.{ PreconditionException, PreconditionExceptions }
import app.commons.validation.{ ErrorOr, ThrowableEither }
import io.circe.Encoder
import org.http4s.Response
import org.http4s.dsl.io._
import io.circe.syntax._
import org.http4s.circe._

trait JsonPresenter[F[_], A] {
  def execute(arg: F[A]): IO[Response[IO]]

  protected def handleErrorOr[R: Encoder](errorOr: ErrorOr[R]): IO[Response[IO]] =
    errorOr.fold[IO[Response[IO]]](
      failure,
      success[R]
    )

  protected def handleErrorOrEt[R: Encoder](
      errorOrEt: ErrorOr[ThrowableEither[R]]): IO[Response[IO]] =
    errorOrEt.fold[IO[Response[IO]]](
      failure,
      handleEt
    )

  protected def handleEt[R: Encoder](et: ThrowableEither[R]): IO[Response[IO]] =
    et.fold(
      handleException,
      success[R]
    )

  protected def handleException(t: Throwable) = t match {
    case PreconditionExceptions(errors) => BadRequest(errors.toList.mkString(","))
    case PreconditionException(m)       => BadRequest(m)
    case e: Exception                   => InternalServerError(e.getMessage)
  }

  private def failure(errors: NonEmptyList[String]) =
    BadRequest(errors.toList.mkString(","))

  private def success[R: Encoder](result: R): IO[Response[IO]] =
    Ok(result.asJson)
}
