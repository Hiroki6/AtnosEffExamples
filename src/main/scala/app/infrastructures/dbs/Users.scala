package app.infrastructures.dbs

import java.time.LocalDateTime

import argonaut.Argonaut.casecodec3
import argonaut.CodecJson
import doobie.implicits._
import doobie.util.fragments._
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio._

case class Users(id: String, name: String, createdAt: LocalDateTime)

object Users {
  implicit val userCodec: CodecJson[Users] =
    casecodec3(Users.apply, Users.unapply)("id", "name", "created_at")

  private val baseSql = fr"select id, name, created_at from users"

  def finds[R: _connectionIO](): Eff[R, Seq[Users]] = fromConnectionIO {
    baseSql
      .query[Users]
      .to[Seq]
  }

  def find[R: _connectionIO](id: String): Eff[R, Option[Users]] =
    fromConnectionIO {
      (baseSql ++ whereAnd(fr"id = $id"))
        .query[Users]
        .option
    }

  def findByEmail[R: _connectionIO](email: String): Eff[R, Option[Users]] = fromConnectionIO {
    (baseSql ++ whereAnd(fr"email = $email"))
      .query[Users]
      .option
  }

  def create[R: _connectionIO](id: String, name: String, createdAt: LocalDateTime): Eff[R, Int] =
    fromConnectionIO {
      sql"""
          insert into users(id, name, created_at) values ($id, $name, $createdAt)
      """.update.run
    }
}
