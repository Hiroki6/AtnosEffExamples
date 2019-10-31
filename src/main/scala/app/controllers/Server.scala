package app.controllers

import cats.effect.{ ExitCode, IO, IOApp }
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import cats.syntax.functor._
import org.http4s.implicits._

object Server extends IOApp {
  val httpApp =
    Router("/" -> HelloWorld.service, "/posts" -> CreatePostController.service).orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}
