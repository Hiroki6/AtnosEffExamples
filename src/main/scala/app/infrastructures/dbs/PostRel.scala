package app.infrastructures.dbs

import java.time.LocalDateTime

import argonaut.Argonaut.casecodec3
import argonaut.CodecJson
import doobie.ConnectionIO
import doobie.implicits._

case class PostRel(
    parentId: String,
    childId: String,
    createdAt: LocalDateTime
)

object PostRel {
  implicit val postRelCodec: CodecJson[PostRel] =
    casecodec3(PostRel.apply, PostRel.unapply)("parent_id", "child_id", "created_at")

  private val baseSql = fr"select parent_id, child_id, created_at from post_rel"

  def create(parentId: String, childId: String, createdAt: LocalDateTime): ConnectionIO[Int] = {
    sql"""
          insert into post_rel(parent_id, child_id, created_at) values ($parentId, $childId, $createdAt)
      """.update.run
  }
}
