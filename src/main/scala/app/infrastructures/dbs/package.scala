package app.infrastructures

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import argonaut.Argonaut.casecodec1
import argonaut.CodecJson
import doobie.util.Meta

package object dbs {
  implicit val localDateTimeMeta: Meta[LocalDateTime] =
    Meta[java.sql.Timestamp]
      .timap[LocalDateTime](timestamp => timestamp.toLocalDateTime)(localDateTime =>
        java.sql.Timestamp.valueOf(localDateTime))

  implicit val localDateCodec: CodecJson[LocalDateTime] = {
    casecodec1[String, LocalDateTime](
      LocalDateTime.parse(_, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")),
      (d: LocalDateTime) => Option(d.toString))("datetime")
  }
}
