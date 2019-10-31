package app.infrastructures.dbs

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import cats.syntax.list._
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.fragment
import doobie.util.fragments._
import doobie.util.query.Query0

case class Posts(
    id: String,
    userId: String,
    text: String,
    postedAt: LocalDateTime,
    parentIdOpt: Option[String],
    childIds: Seq[String] = Nil
)

object Posts {
  private val baseSql =
    fr"""
      |select p.id, p.user_id, p.text, p.posted_at, pa.parent_id, ch.child_ids from posts p left join
      |(select p.id, pr.parent_id from posts p inner join post_rel pr on p.id = pr.parent_id) pa on p.id = pa.id left join
      |(select p.id, group_concat(pr.child_id) as child_ids from posts p inner join post_rel pr on p.id = pr.child_id group by p.id) ch on p.id = ch.id
    """.stripMargin

  private def extractAll(results: Query0[Posts]): ConnectionIO[Seq[Posts]] =
    results.to[Seq]

  private def extractOpt(results: Query0[Posts]): ConnectionIO[Option[Posts]] =
    results.option

  private def getPosts(frg: fragment.Fragment): Query0[(Posts)] =
    //frg.query[Posts]
    frg.query[(String, String, String, String, Option[String], Option[String])].map {
      case (id, userId, text, postedAt, parentId, childIdsOpt) =>
        Posts(
          id,
          userId,
          text,
          LocalDateTime.parse(postedAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")),
          parentId,
          childIdsOpt.fold[Seq[String]](Nil) { _.split(",").toSeq }
        )
    }

  def findAll(): ConnectionIO[Seq[Posts]] =
    extractAll {
      getPosts(baseSql)
    }

  def findAllByIds(ids: Seq[String]): ConnectionIO[Seq[Posts]] =
    extractAll {
      getPosts(baseSql ++ whereAndOpt(ids.toList.toNel.map(i => in(fr"id", i))))
    }

  def find(id: String): ConnectionIO[Option[Posts]] =
    extractOpt {
      getPosts(baseSql ++ whereAnd(fr"id = $id"))
    }

  def create(id: String, userId: String, text: String, postedAt: LocalDateTime): ConnectionIO[Int] =
    sql"""
         insert into posts(id, user_id, text, posted_at) values ($id, $userId, $text, $postedAt)
       """.update.run

}
