package com.github.fedeoasi.newsapi

import java.time.Instant

import com.github.fedeoasi.newsapi.NewsApiClient.Response
import com.neovisionaries.i18n.{CountryCode, LanguageCode}

import scala.concurrent.{ExecutionContext, Future}

class AsyncNewsApiClient private[newsapi] (newsApiClient: NewsApiClient)(implicit context: ExecutionContext) {
  def topHeadlines(
    query: Option[String] = None,
    country: Option[CountryCode] = None,
    category: Option[Category] = None,
    sources: Seq[String] = Seq.empty,
    pageSize: Option[Int] = None,
    page: Option[Int] = None): Future[Response[ArticlesResponse]] = {

    Future { newsApiClient.topHeadlines(query, country, category, sources, pageSize, page) }
  }

  def everything(
    query: String,
    sources: Seq[String] = Seq.empty,
    domains: Seq[String] = Seq.empty,
    from: Option[Instant] = None,
    to: Option[Instant] = None,
    language: Option[LanguageCode] = None,
    sortBy: Option[SortBy] = None,
    pageSize: Option[Int] = None,
    page: Option[Int] = None): Future[Response[ArticlesResponse]] = {

    Future { newsApiClient.everything(query, sources, domains, from, to, language, sortBy, pageSize, page) }
  }

  def sources(
    category: Option[Category] = None,
    language: Option[LanguageCode] = None,
    country: Option[CountryCode] = None
  ): Future[Response[SourcesResponse]] = {

    Future { newsApiClient.sources(category, language, country) }
  }
}

object AsyncNewsApiClient {
  def apply(
    apiKey: String,
    host: String = "newsapi.org",
    useHttps: Boolean = true)(implicit context: ExecutionContext): AsyncNewsApiClient = {

    new AsyncNewsApiClient(NewsApiClient(apiKey, host, useHttps))
  }
}
