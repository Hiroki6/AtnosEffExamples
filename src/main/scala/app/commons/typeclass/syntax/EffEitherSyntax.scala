package app.commons.typeclass.syntax

import app.commons.typeclass.EffEither
import org.atnos.eff.Eff
import org.atnos.eff.either._throwableEither

trait EffEitherSyntax {
  implicit final def syntaxEffEither[R: _throwableEither, F[_], A](fa: Eff[R, F[A]]) =
    new EffEitherOps[R, F, A](fa)
}

final class EffEitherOps[R: _throwableEither, F[_], A](private val fa: Eff[R, F[A]]) {
  def error(message: String)(implicit E: EffEither[F, A]): Eff[R, A] =
    E.error(fa)(message)
}
