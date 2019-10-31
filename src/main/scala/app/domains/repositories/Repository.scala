package app.domains.repositories

import app.domains.Id
import domains.Entity
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio._connectionIO

trait Repository[E <: Entity] {
  def resolve[R: _connectionIO](id: Id[E]): Eff[R, Option[E]]

  def store[R: _connectionIO](entity: E): Eff[R, Unit]
}
