package examples.mtl

import cats.data.EitherT
import cats.instances.option._

object DoubleStack {
  def program: EitherT[Option, String, Int] =
    for {
      t <- EitherT.fromEither[Option](Right(1))
      result <- EitherT.liftF[Option, String, Int](Some(t))
    } yield result

  program.value // Option[Either[String, Int]]
}
