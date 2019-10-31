package app.commons

import pureconfig._
import pureconfig.generic.auto._

object Config {
  val config = loadConfigOrThrow[Config]
}

case class Config(database: Database)
case class Database(driver: String, url: String, user: String, password: String)
