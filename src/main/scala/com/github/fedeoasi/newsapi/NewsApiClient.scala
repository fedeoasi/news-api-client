package com.github.fedeoasi.newsapi

import com.github.fedeoasi.newsapi.NewsApiClient.Params
import com.neovisionaries.i18n.{CountryCode, LanguageCode}
import org.json4s.jackson.Serialization._

import scalaj.http.{Http, HttpRequest, HttpResponse}

class NewsApiClient(apiKey: String, host: String = "newsapi.org", useHttps: Boolean = true) {
  import NewsApiClient.Params._

  private val protocol = if (useHttps) "https" else "http"
  private val Host = s"$protocol://$host/v2"
  private implicit val formats = org.json4s.DefaultFormats

  def topHeadlines(
    query: Option[String] = None,
    country: Option[CountryCode] = None,
    category: Option[Category] = None,
    sources: Seq[String] = Seq.empty,
    pageSize: Option[Int] = None,
    page: Option[Int] = None): Either[String, ArticlesResponse] = {

    val request = Http(s"$Host/top-headlines").param(ApiKey, apiKey)
    val addQueryParams = Function.chain[HttpRequest](Seq(
      addOptionalQueryParameter(_, Query, query),
      addOptionalQueryParameter(_, Country, country.map(_.getAlpha2)),
      addOptionalQueryParameter(_, Params.Category, category.map(_.name())),
      addOptionalQueryParameter(_, Sources, toCsv(sources)),
      addOptionalQueryParameter(_, PageSize, pageSize.map(_.toString())),
      addOptionalQueryParameter(_, Page, page.map(_.toString()))
    ))
    val response = addQueryParams(request).asString
    parseResponse(response)
  }

  def everything(
    query: Option[String] = None,
    sources: Seq[String] = Seq.empty,
    domains: Seq[String] = Seq.empty,
    from: Option[String] = None,
    to: Option[String] = None,
    language: Option[LanguageCode] = None,
    sortBy: Option[SortBy] = None,
    pageSize: Option[Int] = None,
    page: Option[Int] = None): Either[String, ArticlesResponse] = {

    val request = Http(s"$Host/everything")
      .param(ApiKey, apiKey)
    val addQueryParams = Function.chain[HttpRequest](Seq(
      addOptionalQueryParameter(_, Query, query),
      addOptionalQueryParameter(_, Sources, toCsv(sources)),
      addOptionalQueryParameter(_, Domains, toCsv(domains)),
      addOptionalQueryParameter(_, From, from),
      addOptionalQueryParameter(_, To, to),
      addOptionalQueryParameter(_, Language, language.map(_.name())),
      addOptionalQueryParameter(_, Params.SortBy, sortBy.map(_.name())),
      addOptionalQueryParameter(_, PageSize, pageSize.map(_.toString())),
      addOptionalQueryParameter(_, Page, page.map(_.toString()))
    ))
    val response = addQueryParams(request).asString
    parseResponse(response)
  }

  private def addOptionalQueryParameter(request: HttpRequest, key: String, value: Option[String]): HttpRequest = {
    value match {
      case Some(v) => request.param(key, v)
      case None => request
    }
  }

  private def toCsv(seq: Seq[String]): Option[String] = if (seq.nonEmpty) Some(seq.mkString(",")) else None

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
    val Country = "country"
    val Category = "category"
    val Sources = "sources"
    val Domains = "sources"
    val From = "from"
    val To = "to"
    val Language = "language"
    val SortBy = "sortBy"
    val PageSize = "pageSize"
    val Page = "page"
  }
}
