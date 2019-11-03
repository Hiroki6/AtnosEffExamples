package app.commons

import cats.data.ValidatedNel
import org.atnos.eff.MemberIn.|=
import org.atnos.eff.{ Eff, Validate }
import org.atnos.eff.either._

object validation {
  type ThrowableEither[A] = Either[Throwable, A]
  type ErrorOr[A] = ValidatedNel[String, A]
  type Validated[A] = Validate[String, A]
  type _errorOr[R] = Validated |= R

  /** create an Either effect from a single ValidatedNel[String, A] */
  def validateEither[R: _throwableEither, A](validated: ValidatedNel[String, A]): Eff[R, A] = {
    validated.fold(
      errors => left(PreconditionExceptions(errors)),
      value => right(value)
    )
  }
}
