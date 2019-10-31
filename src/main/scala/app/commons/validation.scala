package app.commons

import cats.data.ValidatedNel
import org.atnos.eff.MemberIn.|=
import org.atnos.eff.Validate

object validation {
  type ThrowableEither[A] = Either[Throwable, A]
  type ErrorOr[A] = ValidatedNel[String, A]
  type Validated[A] = Validate[String, A]
  type _errorOr[R] = Validated |= R
}
