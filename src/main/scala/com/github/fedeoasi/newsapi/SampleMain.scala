package com.github.fedeoasi.newsapi

import com.neovisionaries.i18n.CountryCode

object SampleMain {
  def main(args: Array[String]): Unit = {
    val NewsApiKeyEnv = "NEWS_API_KEY"
    Option(System.getenv(NewsApiKeyEnv)) match {
      case Some(apiKey) =>
        val client = NewsApiClient(apiKey)
        val Right(response) = client.topHeadlines(country = Some(CountryCode.US))
        println(s"Found ${response.totalResults} headlines.")
        response.articles.foreach(a => println(s"${a.publishedAt} - ${a.source.name} - ${a.title} - ${a.content}"))
      case None =>
        throw new RuntimeException(s"Please provide a valid api key as $NewsApiKeyEnv")
    }
  }
}
