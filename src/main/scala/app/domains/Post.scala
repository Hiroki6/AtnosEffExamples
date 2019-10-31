package app.domains

import java.time.LocalDateTime

import app.domains.Post.Text
import app.commons.validation._errorOr
import domains.Entity
import org.atnos.eff.Eff
import org.atnos.eff.validate.validateValue

case class Post(
    id: Id[Post],
    userId: Id[User],
    text: Text,
    childIds: Seq[Id[Post]],
    parentId: Option[Id[Post]],
    postedAt: LocalDateTime
) extends Entity

object Post {
  def create[R: _errorOr](userId: String,
                          text: String,
                          parentPostId: Option[String]): Eff[R, Post] = {
    for {
      text <- Text.create(text)
    } yield {
      new Post(
        Id.generate[Post](),
        Id(userId),
        text,
        Seq.empty[Id[Post]],
        parentPostId.map(Id(_)),
        LocalDateTime.now()
      )
    }
  }

  case class Text(value: String) extends AnyVal
  object Text {
    private[domains] def create[R: _errorOr](text: String): Eff[R, Text] = {
      validateValue(text.length <= 140, Text(text), "text length is over 140")
    }
  }
}
