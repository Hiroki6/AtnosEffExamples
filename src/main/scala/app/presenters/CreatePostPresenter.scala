package app.presenters

import app.services.CreatePostDTO
import cats.Id
import cats.effect.IO
import app.commons.validation.{ ErrorOr, ThrowableEither }
import org.http4s.circe.jsonEncoderOf
import io.circe.generic.auto._
import org.http4s.Response

trait CreatePostPresenter extends JsonPresenter[Id, ErrorOr[CreatePostDTO]] {
  implicit val encoder = jsonEncoderOf[IO, CreatePostDTO]

  def execute(arg: ThrowableEither[ErrorOr[ThrowableEither[CreatePostDTO]]]): IO[Response[IO]] = {
    arg.fold(
      handleException,
      handleErrorOrEt[CreatePostDTO]
    )
  }
}

private object CreatePostPresenter extends CreatePostPresenter

trait UsesCreatePostPresenter {
  val createPostPresenter: CreatePostPresenter
}

trait MixInCreatePostPresenter {
  val createPostPresenter: CreatePostPresenter = CreatePostPresenter
}
