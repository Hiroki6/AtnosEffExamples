package app.services

import app.domains.User
import app.commons.validation._errorOr
import org.atnos.eff.Eff
import org.atnos.eff.validate.validateOption

trait CommandService[R, In, Out] {
  def execute(in: In): Eff[R, Out]

  protected def authenticate[R: _errorOr](userOpt: Option[User]): Eff[R, Unit] = {
    validateOption(userOpt, "user is not exists.")
  }
}
