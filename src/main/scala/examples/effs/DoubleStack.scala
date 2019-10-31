package examples.effs

import org.atnos.eff.MemberIn.|=
import org.atnos.eff.{ Eff, Fx }
import org.atnos.eff.either.fromEither
import org.atnos.eff.option.{ _option, fromOption }
import org.atnos.eff.syntax.either._
import org.atnos.eff.syntax.option._
import org.atnos.eff.syntax.eff._

object DoubleStack {
  type StringEither[A] = String Either A
  type _stringEither[R] = StringEither |= R

  def program[R: _stringEither: _option]: Eff[R, Int] =
    for {
      t <- fromEither[R, String, Int](Right(1))
      result <- fromOption[R, Int](Some(t))
    } yield result

  /** Stack declaration **/
  type Stack = Fx.fx2[Option, StringEither]
  program[Stack] // Eff[Fx2[Option, StringEither], Int]
  .runOption // Eff[Fx1[StringEither], Option[Int]]
  .runEither // Eff[NoFx, Either[String, Option[Int]]
  .run // Either[String, Option[Int]
}
