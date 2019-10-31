package app.domains.repositories

import app.domains.{ Id, Post }
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio._connectionIO

trait PostRepository extends Repository[Post] {
  def findAll[R: _connectionIO](): Eff[R, Seq[Post]]

  def findAllBy[R: _connectionIO](ids: Seq[Id[Post]]): Eff[R, Seq[Post]]
}
