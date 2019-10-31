package app.controllers

import java.util.concurrent.ForkJoinPool

import cats.effect.{ ContextShift, IO, Timer }

import scala.concurrent.ExecutionContext

trait Controller {
  implicit val ec: ExecutionContext = ExecutionContext.fromExecutor(new ForkJoinPool(8))
  implicit val cs: ContextShift[IO] = IO.contextShift(ec)
  implicit val timer: Timer[IO] = IO.timer(ec)
}
