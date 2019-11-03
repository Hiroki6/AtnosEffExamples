package app.services

import app.commons.PreconditionException
import app.commons.validation.validateEither
import app.domains.repositories.{ PostRepository, UserRepository }
import app.domains.{ Id, Post }
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio._connectionIO
import org.atnos.eff.addon.cats.effect.IOEffect._
import org.atnos.eff.either._throwableEither
import org.atnos.eff.either._

class CreatePostService[R: _io: _connectionIO: _throwableEither](
    implicit postRepository: PostRepository,
    userRepository: UserRepository)
    extends CommandService[R, CreatePostParam, CreatePostDTO] {

  override def execute(in: CreatePostParam): Eff[R, CreatePostDTO] = {
    for {
      userOpt <- userRepository.resolve[R](Id(in.userId))
      _ <- optionEither(userOpt, PreconditionException("user is not exist."))
      post <- validateEither(Post.create(in.userId, in.text, None))
      _ <- postRepository.store[R](post)
    } yield CreatePostDTO(post.id.value)
  }

}

case class CreatePostParam(userId: String, text: String)
case class CreatePostDTO(id: String)

object CreatePostService {
  def apply[R: _io: _connectionIO: _throwableEither](implicit postRepository: PostRepository,
                                                     userRepository: UserRepository) =
    new CreatePostService
}
