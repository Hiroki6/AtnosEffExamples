package app.infrastructures.dbs.repositories

import app.domains._
import app.domains.repositories.UserRepository
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio._
import app.infrastructures.dbs.Users
import app.commons.typeclass.syntax.effFunc._
import app.commons.typeclass.instances.option._

class UserRepositoryOnRDBMS extends UserRepository {
  override def resolve[R: _connectionIO](id: Id[User]): Eff[R, Option[User]] =
    Users.find(id.value).mapE(toEntity)

  override def store[R: _connectionIO](entity: User): Eff[R, Unit] =
    Users
      .create(
        entity.id.value,
        entity.name.value,
        entity.createdAt
      )
      .map(_ => ())

  private def toEntity(users: Users): User = {
    User(
      Id(users.id),
      Name(users.name),
      users.createdAt
    )
  }
}

trait UsesUserRepository {}
