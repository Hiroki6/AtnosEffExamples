package app.infrastructures.dbs

import java.util.concurrent.Executors

import cats.effect.IO
import doobie.util.transactor.Transactor
import app.commons.Config

import scala.concurrent.ExecutionContext

object Database {
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(8));
  implicit val cs = IO.contextShift(ec)
  implicit lazy val xa: Transactor.Aux[IO, Unit] = {
    val database = Config.config.database
    Transactor.fromDriverManager[IO](
      database.driver,
      database.url,
      database.user,
      database.password
    )
  }
}
