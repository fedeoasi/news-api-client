package com.github.fedeoasi

import java.time.Instant

import com.neovisionaries.i18n.LanguageCode
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
    urlToImage: String,
    content: String)

  case class Source(id: Option[String], name: String)

  case class FullSource(
    id: String,
    name: String,
    description: String,
    url: String,
    category: Category,
    language: LanguageCode,
    country: String)

  object InstantSerializer extends CustomSerializer[Instant](_ => (
    { case JString(s) => Instant.parse(s) },
    { case t: Instant => JString(t.toString) }
  ))

  object CategorySerializer extends CustomSerializer[Category](_ => (
    { case JString(c) => Category.valueOf(c) },
    { case c: Category => JString(c.name()) }
  ))

  object LanguageCodeSerializer extends CustomSerializer[LanguageCode](_ => (
    { case JString(c) => LanguageCode.valueOf(if (c == "ud") "ur" else c) },
    { case c: LanguageCode => JString(c.name()) }
  ))
}
