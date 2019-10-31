package app.commons.typeclass

import org.atnos.eff.Eff
import org.atnos.eff.either._

trait EffEither[F[_], A] {
  def error[R: _throwableEither](fa: Eff[R, F[A]])(message: String): Eff[R, A]
}
