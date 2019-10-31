package app.commons.typeclass

import app.commons.validation._errorOr
import org.atnos.eff.Eff

trait EffValidate[F[_], A] {
  def validate[R: _errorOr](fa: Eff[R, F[A]])(message: String): Eff[R, Unit]
}
