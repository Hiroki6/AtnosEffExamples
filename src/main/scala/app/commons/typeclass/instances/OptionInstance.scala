package app.commons.typeclass.instances

import app.commons.PreconditionException
import app.commons.typeclass.{ EffEither, EffFunc, EffValidate }
import org.atnos.eff.Eff
import org.atnos.eff.syntax.all._
import cats.instances.option._
import app.commons.validation._errorOr
import org.atnos.eff.validate._
import org.atnos.eff.either._

trait OptionInstance {
  implicit final def optionEffFunc[A] = new EffFunc[Option, A] {
    override def flatMapE[R, B](fa: Eff[R, Option[A]])(g: A => Eff[R, B]): Eff[R, Option[B]] = {
      fa.flatMap(_.traverseA(g))
    }

    override def mapE[R, B](fa: Eff[R, Option[A]])(g: A => B): Eff[R, Option[B]] = {
      fa.map(_.map(g))
    }
  }

  implicit final def optionEffValidate[A] = new EffValidate[Option, A] {
    override def validate[R: _errorOr](fa: Eff[R, Option[A]])(message: String): Eff[R, Unit] =
      for {
        optValue <- fa
      } yield validateOption(optValue, message)
  }

  implicit final def optionEffEither[A] = new EffEither[Option, A] {
    override def error[R: _throwableEither](fa: Eff[R, Option[A]])(message: String): Eff[R, A] = {
      fa.flatMap {
        case None    => left(PreconditionException(message))
        case Some(v) => right(v)
      }
    }
  }
}
