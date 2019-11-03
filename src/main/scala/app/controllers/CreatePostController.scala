package app.controllers

import cats.effect.IO
import app.commons.SnakeCaseJsonSupport
import app.infrastructures.dbs.Database
import doobie.free.connection.ConnectionIO
import app.infrastructures.dbs.repositories.{ PostRepositoryOnRDBMS, UserRepositoryOnRDBMS }
import io.circe.generic.auto._
import io.circe.generic.extras.ConfiguredJsonCodec
import org.atnos.eff._
import org.atnos.eff.syntax.addon.cats.effect._
import org.atnos.eff.syntax.addon.doobie._
import org.http4s.{ EntityDecoder, HttpRoutes, Request, Response }
import org.http4s.circe._
import org.atnos.eff.syntax.either._
import app.presenters.{ MixInCreatePostPresenter, UsesCreatePostPresenter }
import app.services.{ CreatePostDTO, CreatePostParam, CreatePostService }
import app.commons.validation.ThrowableEither
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io.{ ->, /, POST }

trait ICreatePostController extends Controller with UsesCreatePostPresenter {
  implicit val postRepository = new PostRepositoryOnRDBMS
  implicit val userRepository = new UserRepositoryOnRDBMS

  type R = Fx.fx3[IO, ConnectionIO, ThrowableEither]

  val service = HttpRoutes.of[IO] {
    case req @ POST -> Root / "create" =>
      CreatePostController.execute(req)
  }

  def execute(request: Request[IO]): IO[Response[IO]] =
    for {
      createPostRequest <- request.as[CreatePostRequest]
      createPostParam = CreatePostParam(createPostRequest.userId, createPostRequest.text)
      createPostDTO <- run(createPostParam)
      response <- createPostPresenter.execute(createPostDTO)
    } yield {
      response
    }

  private def run(
      createPostParam: CreatePostParam): IO[ThrowableEither[ThrowableEither[CreatePostDTO]]] = {
    CreatePostService[R]
      .execute(createPostParam)
      .runEither[Throwable]
      .runConnectionIO(Database.xa)
      .ioAttempt
      .to[IO]
  }

}

object CreatePostController extends ICreatePostController with MixInCreatePostPresenter

@ConfiguredJsonCodec
case class CreatePostRequest(userId: String, text: String)

object CreatePostRequest extends SnakeCaseJsonSupport {
  implicit lazy val decoder: EntityDecoder[IO, CreatePostRequest] = jsonOf[IO, CreatePostRequest]
}
