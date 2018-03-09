package com.github.fedeoasi.newsapi

import com.github.fedeoasi.newsapi.NewsApiClient.Params
import com.neovisionaries.i18n.{CountryCode, LanguageCode}
import org.json4s.jackson.Serialization._

import scalaj.http.{Http, HttpRequest, HttpResponse}

class NewsApiClient(apiKey: String, host: String = "newsapi.org") {
  import NewsApiClient.Params._

  private val Host = s"http://$host/v2"
  private implicit val formats = org.json4s.DefaultFormats

  def topHeadlines(
    query: Option[String] = None,
    country: Option[CountryCode] = None,
    category: Option[Category] = None,
    sources: Seq[String] = Seq.empty,
    pageSize: Option[Int] = None,
    page: Option[Int] = None): Either[String, ArticlesResponse] = {

    val request = Http(s"$Host/top-headlines")
      .param(ApiKey, apiKey)
    val withQuery = addOptionalQueryParameter(request, Query, query)
    val withCountry = addOptionalQueryParameter(withQuery, Country, country.map(_.getAlpha2))
    val withCategory = addOptionalQueryParameter(withCountry, Params.Category, category.map(_.name()))
    val withSources = addOptionalQueryParameter(withCategory, Sources, toCsv(sources))
    val withPageSize = addOptionalQueryParameter(withSources, PageSize, pageSize.map(_.toString()))
    val withPage = addOptionalQueryParameter(withPageSize, Page, page.map(_.toString()))
    val response = withPage.asString
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
    val withQuery = query.map(request.param(Query, _)).getOrElse(request)
    val withSources = addOptionalQueryParameter(withQuery, Sources, toCsv(sources))
    val withDomains = addOptionalQueryParameter(withSources, Domains, toCsv(domains))
    val withFrom = addOptionalQueryParameter(withDomains, From, from)
    val withTo = addOptionalQueryParameter(withFrom, To, to)
    val withLanguage = addOptionalQueryParameter(withTo, Language, language.map(_.name()))
    val withSortBy = addOptionalQueryParameter(withLanguage, Params.SortBy, sortBy.map(_.name()))
    val withPageSize = addOptionalQueryParameter(withSortBy, PageSize, pageSize.map(_.toString()))
    val withPage = addOptionalQueryParameter(withPageSize, Page, page.map(_.toString()))
    val response = withPage.asString
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
