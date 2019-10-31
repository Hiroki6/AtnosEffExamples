package app.domains

import java.util.UUID

import domains.Entity

case class Id[A <: Entity](value: String) extends AnyVal

object Id {
  def generate[E <: Entity](): Id[E] = Id(UUID.randomUUID().toString)
}
