package app.commons.typeclass.syntax

import app.commons.typeclass.EffFunc
import org.atnos.eff.Eff

trait EffFuncSyntax {
  implicit final def syntaxEff[R, F[_], A](fa: Eff[R, F[A]]) = new EffOps[R, F, A](fa)
}

final class EffOps[R, F[_], A](private val fa: Eff[R, F[A]]) extends AnyVal {
  def flatMapE[B](g: A => Eff[R, B])(implicit E: EffFunc[F, A]): Eff[R, F[B]] =
    E.flatMapE(fa)(g)

  def mapE[B](g: A => B)(implicit E: EffFunc[F, A]): Eff[R, F[B]] =
    E.mapE(fa)(g)
}
