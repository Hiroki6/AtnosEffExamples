package app.services

import app.commons.validation._errorOr
import app.domains.repositories.{ PostRepository, UserRepository }
import app.domains.{ Id, Post }
import org.atnos.eff.Eff
import org.atnos.eff.addon.doobie.connectionio._connectionIO
import org.atnos.eff.addon.cats.effect.IOEffect._
import app.commons.typeclass.syntax.effValidate.syntaxEffValidate
import app.commons.typeclass.instances.option._

class CreatePostService[R: _io: _connectionIO: _errorOr](implicit postRepository: PostRepository,
                                                         userRepository: UserRepository)
    extends CommandService[R, CreatePostParam, CreatePostDTO] {

  override def execute(in: CreatePostParam): Eff[R, CreatePostDTO] = {
    for {
      _ <- userRepository.resolve[R](Id(in.userId)).validate("user is not exist.")
      post <- Post.create[R](in.userId, in.text, None)
      _ <- postRepository.store[R](post)
    } yield CreatePostDTO(post.id.value)
  }

}

case class CreatePostParam(userId: String, text: String)
case class CreatePostDTO(id: String)

object CreatePostService {
  def apply[R: _io: _connectionIO: _errorOr](implicit postRepository: PostRepository,
                                             userRepository: UserRepository) =
    new CreatePostService
}
