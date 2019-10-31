package app.infrastructures.dbs.repositories

import app.domains.Post.Text
import app.domains.repositories.PostRepository
import app.domains.{ Id, Post }
import app.infrastructures.dbs.{ PostRel, Posts }
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio.{ _connectionIO, fromConnectionIO }

class PostRepositoryOnRDBMS extends PostRepository {
  def resolve[R: _connectionIO](id: Id[Post]): Eff[R, Option[Post]] = fromConnectionIO {
    Posts.find(id.value).map(_.map(toEntity))
  }

  def store[R: _connectionIO](entity: Post): Eff[R, Unit] = fromConnectionIO {
    Posts.create(entity.id.value, entity.userId.value, entity.text.value, entity.postedAt).map {
      _ =>
        entity.parentId.map { parentId =>
          PostRel.create(parentId.value, entity.id.value, entity.postedAt)
        }
    }
  }

  def findAll[R: _connectionIO](): Eff[R, Seq[Post]] = {
    for {
      postz <- fromConnectionIO(Posts.findAll())
    } yield toEntities(postz)
  }

  def findAllBy[R: _connectionIO](ids: Seq[Id[Post]]): Eff[R, Seq[Post]] = {
    for {
      postz <- fromConnectionIO(Posts.findAllByIds(ids.map(_.value)))
    } yield toEntities(postz)
  }

  private def toEntities(posts: Seq[Posts]): Seq[Post] =
    for {
      post <- posts
    } yield {
      toEntity(post)
    }

  private def toEntity(post: Posts): Post =
    Post(
      id = Id(post.id),
      userId = Id(post.userId),
      text = Text(post.text),
      childIds = post.childIds.map(Id[Post]),
      parentId = post.parentIdOpt.map(Id[Post]),
      postedAt = post.postedAt
    )
}

trait UsesPostRepositoryOnRDBMS {
  val postRepository: PostRepository
}

trait MixInPostRepositoryOnRDBMS {
  val postRepository: PostRepository = new PostRepositoryOnRDBMS
}
