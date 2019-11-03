package app.presenters

import app.services.CreatePostDTO
import cats.effect.IO
import app.commons.validation.ThrowableEither
import org.http4s.circe.jsonEncoderOf
import io.circe.generic.auto._
import org.http4s.Response

trait CreatePostPresenter extends JsonPresenter[ThrowableEither, ThrowableEither[CreatePostDTO]] {
  implicit val encoder = jsonEncoderOf[IO, CreatePostDTO]

  override def execute(arg: ThrowableEither[ThrowableEither[CreatePostDTO]]): IO[Response[IO]] = {
    arg.fold(
      handleException,
      handleEt[CreatePostDTO]
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
