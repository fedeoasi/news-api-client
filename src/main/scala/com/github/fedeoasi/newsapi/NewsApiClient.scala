package com.github.fedeoasi.newsapi

import org.json4s.jackson.Serialization._

import scalaj.http.{Http, HttpResponse}

class NewsApiClient(apiKey: String, host: String = "newsapi.org") {
  import NewsApiClient.Params._

  private val Host = s"http://$host/v2"
  private implicit val formats = org.json4s.DefaultFormats

  def topHeadlines(
    query: Option[String] = None): Either[String, ArticlesResponse] = {

    val request = Http(s"$Host/top-headlines")
      .param(ApiKey, apiKey)
    val requestWithQuery = query.map(request.param(Query, _)).getOrElse(request)
    val response = requestWithQuery.asString
    parseResponse(response)
  }

  def everything(
    query: Option[String] = None): Either[String, ArticlesResponse] = {

    val request = Http(s"$Host/everything")
      .param(ApiKey, apiKey)
    val requestWithQuery = query.map(request.param(Query, _)).getOrElse(request)
    val response = requestWithQuery.asString
    parseResponse(response)
  }

  private def parseResponse(response: HttpResponse[String]) = {
    if (!response.is2xx) {
      Left(s"Error: returned code is ${response.code}. body: ${response.body}")
    } else {
      val body = response.body
      Right(read[ArticlesResponse](body))
    }
  }
}

case class ArticlesResponse(
  articles: Seq[Article],
  totalResults: Int,
  status: String)

object NewsApiClient {
  object Params {
    val ApiKey = "apiKey"
    val Query = "q"
  }
}
