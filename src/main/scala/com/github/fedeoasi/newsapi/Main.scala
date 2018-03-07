package com.github.fedeoasi.newsapi

object Main {
  def main(args: Array[String]): Unit = {
    val NewsApiKeyEnv = "NEWS_API_KEY"
    Option(System.getenv(NewsApiKeyEnv)) match {
      case Some(apiKey) =>
        val client = new NewsApiClient(apiKey)
        val Right(response) = client.everything(Some("formula 1"))
        response.articles.foreach(println)
        println(response)
      case None =>
        throw new RuntimeException(s"Please provide a valid api key as $NewsApiKeyEnv")
    }
  }
}
