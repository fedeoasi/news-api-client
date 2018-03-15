package com.github.fedeoasi.newsapi

import com.github.fedeoasi.newsapi.NewsApiClient.Params._
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.{FunSpec, Matchers}
import java.time.Instant

import com.github.fedeoasi.newsapi.NewsApiClient.Params
import com.neovisionaries.i18n.LanguageCode

class NewsApiClientTest extends FunSpec with Matchers with WiremockSpec {
  private val validApiKey = "validApiSecret"

  private val article1 = """
    {"source":{"id":null,"name":"Formula1.com"},"author":null,"title":"Formula 1 article",
     "description":"A description","url":"url","urlToImage":"imageUrl","publishedAt":"2018-01-31T13:16:33Z"}"""
  private val article2 = """
    {"source":{"id":"cnbc","name":"CNBC"},"author":null,"title":"CNBC Article",
     "description":"A description","url":"url","urlToImage":"imageUrl","publishedAt":"2018-01-31T12:16:33Z"}"""

  private val formulaOneSource = Source(None, "Formula1.com")
  private val expectedArticle1 = Article("Formula 1 article", formulaOneSource, None, "A description", Instant.parse("2018-01-31T13:16:33Z"), "url", "imageUrl")
  private val cnbcSource = Source(Some("cnbc"), "CNBC")
  private val expectedArticle2 = Article("CNBC Article", cnbcSource, None, "A description", Instant.parse("2018-01-31T12:16:33Z"), "url", "imageUrl")

  it("fails when the provided key is invalid") {
    val path = "/v2/everything"
    val invalidApiKey = "123"
    stubFor(get(urlPathEqualTo(path))
      .withQueryParam(ApiKey, equalTo(invalidApiKey))
      .willReturn(
        aResponse()
          .withStatus(401)))
    val client = new NewsApiClient(invalidApiKey, Host, useHttps = false)
    val Left(errorMessage) = client.everything("query")
    errorMessage should include("401")
  }

  describe("Top Headlines") {
    val client = new NewsApiClient(validApiKey, Host, useHttps = false)
    val topHeadlinesPath = "/v2/top-headlines"

    it("finds all headlines") {
      val body = s"""{"status":"ok","totalResults":2,"articles":[$article1,$article2]}"""
      successfulStub(topHeadlinesPath, Seq(ApiKey -> validApiKey), body)
      val Right(response) = client.topHeadlines()
      response.status shouldBe "ok"
      response.totalResults shouldBe 2
      response.articles shouldBe Seq(expectedArticle1, expectedArticle2)
    }

    it("finds all headlines from CNBC") {
      val body = s"""{"status":"ok","totalResults":1,"articles":[$article2]}"""
      successfulStub(topHeadlinesPath, Seq(ApiKey -> validApiKey, Sources -> "cnbc"), body)
      val Right(response) = client.topHeadlines(sources = Seq("cnbc"))
      response.status shouldBe "ok"
      response.totalResults shouldBe 1
      response.articles shouldBe Seq(expectedArticle2)
    }
  }

  describe("Everything") {
    val client = new NewsApiClient(validApiKey, Host, useHttps = false)
    val everythingPath = "/v2/everything"

    it("finds all articles") {
      val body = s"""{"status":"ok","totalResults":3,"articles":[$article1,$article2]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey), body)
      val Right(response) = client.everything("query")
      response.status shouldBe "ok"
      response.totalResults shouldBe 3
      response.articles shouldBe Seq(expectedArticle1, expectedArticle2)
    }

    it("finds only CNBC articles") {
      val body = s"""{"status":"ok","totalResults":1,"articles":[$article2]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey, Sources -> "cnbc"), body)
      val Right(response) = client.everything("query", sources = Seq("cnbc"))
      response.status shouldBe "ok"
      response.totalResults shouldBe 1
      response.articles shouldBe Seq(expectedArticle2)
    }

    it("finds articles before a date") {
      val body = s"""{"status":"ok","totalResults":1,"articles":[$article1]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey, To -> "2018-01-31T13:15:33Z"), body)
      val Right(response) = client.everything("query", to = Some(Instant.parse("2018-01-31T13:15:33Z")))
      response.status shouldBe "ok"
      response.totalResults shouldBe 1
      response.articles shouldBe Seq(expectedArticle1)
    }
  }

  describe("Sources") {
    val client = new NewsApiClient(validApiKey, Host, useHttps = false)
    val everythingPath = "/v2/sources"

    val source1 = """{"id":"abc-news","name":"ABC News","description":"Your trusted source","url":"http://abcnews.go.com","category":"general","language":"en","country":"us"}"""
    val source2 = """{"id":"source2","name":"Second Source","description":"Your trusted source 2","url":"http://source2.com","category":"business","language":"it","country":"it"}"""

    val expectedSource1 = FullSource("abc-news", "ABC News", "Your trusted source", "http://abcnews.go.com", Category.general, LanguageCode.en, "us")
    val expectedSource2 = FullSource("source2", "Second Source", "Your trusted source 2", "http://source2.com", Category.business, LanguageCode.it, "it")

    it("finds all sources") {
      val body = s"""{"status":"ok","sources":[$source1,$source2]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey), body)
      val Right(response) = client.sources()
      response.status shouldBe "ok"
      response.sources shouldBe Seq(expectedSource1, expectedSource2)
    }

    it("finds a source by category") {
      val body = s"""{"status":"ok","sources":[$source1]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey, Params.Category -> Category.general.name()), body)
      val Right(response) = client.sources(category = Some(Category.general))
      response.status shouldBe "ok"
      response.sources shouldBe Seq(expectedSource1)
    }

    it("finds a source by language") {
      val body = s"""{"status":"ok","sources":[$source1]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey, Language -> LanguageCode.en.name), body)
      val Right(response) = client.sources(language = Some(LanguageCode.en))
      response.status shouldBe "ok"
      response.sources shouldBe Seq(expectedSource1)
    }
  }

  private def successfulStub(path: String, queryParams: Seq[(String, String)], body: String): Unit = {
    val builder = get(urlPathEqualTo(path))
    val withParams = queryParams.foldLeft(builder) { case (b, (k, v)) => b.withQueryParam(k, equalTo(v)) }
    stubFor(withParams.willReturn(aResponse().withStatus(200).withBody(body)))
  }
}
