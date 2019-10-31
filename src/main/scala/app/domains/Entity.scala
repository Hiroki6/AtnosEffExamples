package domains

import app.domains.Id

trait Entity {
  val id: Id[_]
}
