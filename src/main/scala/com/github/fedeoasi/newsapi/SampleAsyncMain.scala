package com.github.fedeoasi.newsapi

import com.neovisionaries.i18n.CountryCode

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success}

object SampleAsyncMain {
  def main(args: Array[String]): Unit = {
    val NewsApiKeyEnv = "NEWS_API_KEY"
    Option(System.getenv(NewsApiKeyEnv)) match {
      case Some(apiKey) =>
        import scala.concurrent.ExecutionContext.Implicits.global
        val client = AsyncNewsApiClient(apiKey)
        val allHeadlines = for {
          futureUsHeadlines <- client.topHeadlines(country = Some(CountryCode.US))
          futureItHeadlines <- client.topHeadlines(country = Some(CountryCode.IT))
        } yield (futureUsHeadlines, futureItHeadlines)

        allHeadlines.onComplete {
          case Success((Right(usHeadlines), Right(itHeadlines))) =>
            printHeadlines("US Headlines", usHeadlines)
            printHeadlines("IT Headlines", itHeadlines)
          case Success((Left(usMessage), _)) =>
            println(s"Could not retrieve US headlines: $usMessage")
          case Success((_, Left(itMessage))) =>
            println(s"Could not retrieve IT headlines: $itMessage")
          case Failure(ex) => ex.printStackTrace()
        }
        Await.result(allHeadlines, 10.seconds)
      case None =>
        throw new RuntimeException(s"Please provide a valid api key as $NewsApiKeyEnv")
    }
  }

  def printHeadlines(name: String, response: ArticlesResponse): Unit = {
    println(s"$name: Found ${response.totalResults} headlines.")
    response.articles.foreach(a => println(s"${a.publishedAt} - ${a.source.name} - ${a.title}"))
  }
}
