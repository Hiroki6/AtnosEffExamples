package app.commons.typeclass.syntax

import app.commons.typeclass.EffValidate
import app.commons.validation._errorOr
import org.atnos.eff.Eff

trait EffValidateSyntax {
  implicit final def syntaxEffValidate[R: _errorOr, F[_], A](fa: Eff[R, F[A]]) =
    new EffValidateOps[R, F, A](fa)
}

final class EffValidateOps[R: _errorOr, F[_], A](private val fa: Eff[R, F[A]]) {
  def validate(message: String)(implicit E: EffValidate[F, A]): Eff[R, Unit] =
    E.validate(fa)(message)
}
