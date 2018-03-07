package com.github.fedeoasi

package object newsapi {
  case class Article(
    title: String,
    source: Source,
    author: Option[String],
    description: String,
    //TODO Make this a timestamp
    publishedAt: String,
    url: String,
    urlToImage: String)

  case class Source(id: Option[String], name: String)
}
