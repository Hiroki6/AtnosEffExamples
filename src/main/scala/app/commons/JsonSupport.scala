package app.commons

import io.circe.generic.extras.Configuration

trait SnakeCaseJsonSupport {
  implicit val customConfig: Configuration = Configuration.default.withSnakeCaseMemberNames
}
