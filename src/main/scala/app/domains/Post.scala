package app.domains

import java.time.LocalDateTime

import app.domains.Post.Text
import cats.data.{ Validated, ValidatedNel }
import domains.Entity

case class Post(
    id: Id[Post],
    userId: Id[User],
    text: Text,
    childIds: Seq[Id[Post]],
    parentId: Option[Id[Post]],
    postedAt: LocalDateTime
) extends Entity

object Post {
  def create(userId: String,
             text: String,
             parentPostId: Option[String]): ValidatedNel[String, Post] = {
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
    private[domains] def create(text: String): ValidatedNel[String, Text] = {
      if (text.length() > 140) Validated.invalidNel("text length is over 140")
      else Validated.valid(Text(text))
    }
  }
}
