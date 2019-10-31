package examples.mtl

import cats.data.{ EitherT, OptionT }
import cats.effect.IO
import examples.commons.data._

object TripleStack {
  type EitherIO[A] = EitherT[IO, Error, A]
  type OptionEitherIO[A] = OptionT[EitherIO, A] // OptionT[EitherT[IO, Error, A]]

  def program(userId: UserId, teamName: TeamName): OptionT[EitherIO, Response] = {
    for {
      user <- OptionT[EitherIO, User] {
        EitherT.liftF[IO, Error, Option[User]](findUser(userId))
      }
      team <- OptionT.liftF[EitherIO, Team] {
        EitherT.fromEither[IO](createTeam(userId, teamName))
      }
      _ <- OptionT.liftF[EitherIO, Unit] {
        EitherT.liftF[IO, Error, Unit](storeTeam(team))
      }
    } yield createResponse(user, team)
  }

  program(userId, teamName).value.value.unsafeRunSync() // IO[Either[Error, Option[Response]]]
}
