package examples.mtl

import cats.data.OptionT
import cats.effect.IO
import examples.commons.data._

object OptionTIO {
  def program(userId: UserId): OptionT[IO, Response] =
    for {
      user <- OptionT(findUser(userId))
      team <- OptionT(getTeam(userId))
    } yield createResponse(user, team)

  program(userId).value // IO[Option[Response]]
}
