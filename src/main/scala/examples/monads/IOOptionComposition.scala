package examples.monads

import cats.effect.IO
import examples.commons.data._

object IOOptionComposition {

  /** compile failure
  def program(userId: UserId): IO[Option[Response]] =
    for {
      userOpt <- findUser(userId) // IO[Option[User]]
      teamOpt <- getTeam(userId) // IO[Option[Team]]
      user <- userOpt // Option[User]
      team <- teamOpt // Option[Team]
    } yield createResponse(user, team)
   */
  def program1(userId: UserId): IO[Option[Response]] =
    for {
      userOpt <- findUser(userId)
      teamOpt <- getTeam(userId)
    } yield {
      (userOpt, teamOpt) match {
        case (Some(user), Some(team)) => Some(createResponse(user, team))
        case (_, _)                   => None
      }
    }

  def program2(userId: UserId): IO[Option[Response]] =
    for {
      userOpt <- findUser(userId)
      teamOpt <- getTeam(userId)
    } yield
      for {
        user <- userOpt
        team <- teamOpt
      } yield createResponse(user, team)
}
