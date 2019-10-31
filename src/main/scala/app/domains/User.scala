package app.domains

import java.time.LocalDateTime

import app.commons.validation._errorOr
import org.atnos.eff.Eff
import org.atnos.eff.validate.validateValue
import domains.Entity

case class User(
    id: Id[User],
    name: Name,
    createdAt: LocalDateTime
) extends Entity

object User {
  def create[R: _errorOr](name: String, email: String, rawPassword: String): Eff[R, User] =
    for {
      name <- Name.create(name)
    } yield {
      User(
        Id.generate[User](),
        name,
        LocalDateTime.now()
      )
    }
}

case class Name(value: String) extends AnyVal
object Name {
  def create[R: _errorOr](value: String): Eff[R, Name] = {
    validateValue[R, String, Name](value.length <= 20,
                                   Name(value),
                                   "name must be less than 20 character")
  }
}
