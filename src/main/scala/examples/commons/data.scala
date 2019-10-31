package examples.commons

import cats.effect.IO

object data {
  case class UserId(value: String)
  case class TeamName(value: String)
  case class Team(value: String)
  case class Response(value: String)
  case class Error(value: String)
  case class User(value: String)

  val userId = UserId("test")
  val teamName = TeamName("test")
  def findUser(userId: UserId): IO[Option[User]] = IO {
    Some(User("test"))
  }

  def getTeam(userId: UserId): IO[Option[Team]] = IO {
    Some(Team("team"))
  }

  def createTeam(userId: UserId, teamName: TeamName): Either[Error, Team] = Left(Error("error"))
  def storeTeam(team: Team): IO[Unit] = IO(())

  def createResponse(user: User, team: Team): Response = Response("test")
}
