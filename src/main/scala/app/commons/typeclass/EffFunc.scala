package app.commons.typeclass

import org.atnos.eff.Eff

trait EffFunc[F[_], A] {
  def flatMapE[R, B](fa: Eff[R, F[A]])(g: A => Eff[R, B]): Eff[R, F[B]]
  def mapE[R, B](fa: Eff[R, F[A]])(g: A => B): Eff[R, F[B]]
}
