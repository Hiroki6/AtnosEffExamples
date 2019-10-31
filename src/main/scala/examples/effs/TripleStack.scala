package examples.effs

import cats.effect.IO
import org.atnos.eff.addon.cats.effect.IOEffect.{ _io, fromIO }
import org.atnos.eff.all.{ _option, fromEither, fromOption }
import org.atnos.eff.{ |=, Eff, Fx }
import examples.commons.data._
import org.atnos.eff.syntax.option._
import org.atnos.eff.syntax.either._
import org.atnos.eff.syntax.addon.cats.effect._

object TripleStack {
  type ErrorOr[A] = Either[Error, A]
  type _errorOr[R] = ErrorOr |= R

  def program[R: _io: _option: _errorOr](userId: UserId, teamName: TeamName): Eff[R, Response] =
    for {
      userOpt <- fromIO(findUser(userId))
      user <- fromOption(userOpt)
      team <- fromEither(createTeam(userId, teamName))
      _ <- fromIO(storeTeam(team))
    } yield createResponse(user, team)

  type Stack = Fx.fx3[Option, ErrorOr, IO]

  program[Stack](userId, teamName).runOption // Eff[Fx2[Either, IO], Option[Response]]
    .runEither[Error] // Eff[Fx1[IO],Either[Error, Option[Response]]
    .to[IO] // IO[Either[Error, Option[Response]]]

}
