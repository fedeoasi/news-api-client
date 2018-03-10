package com.github.fedeoasi

import java.time.Instant

import org.json4s.CustomSerializer
import org.json4s.JsonAST.JString

package object newsapi {
  case class Article(
    title: String,
    source: Source,
    author: Option[String],
    description: String,
    publishedAt: Instant,
    url: String,
    urlToImage: String)

  case class Source(id: Option[String], name: String)

  object InstantSerializer extends CustomSerializer[Instant](_ => (
    { case JString(s) => Instant.parse(s) },
    { case t: Instant => JString(t.toString) }
  ))
}
