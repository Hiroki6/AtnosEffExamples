package app.controllers

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.impl.Root
import org.http4s.dsl.io._

object HelloWorld {
  val service = HttpRoutes.of[IO] {
    case GET -> Root =>
      Ok(s"Hello World!")
  }
}
