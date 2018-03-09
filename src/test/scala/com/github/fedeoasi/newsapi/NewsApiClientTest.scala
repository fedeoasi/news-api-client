package com.github.fedeoasi.newsapi

import com.github.fedeoasi.newsapi.NewsApiClient.Params._
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.{FunSpec, Matchers}

class NewsApiClientTest extends FunSpec with Matchers with WiremockSpec {
  private val validApiKey = "validApiSecret"

  private val article1 = """
    {"source":{"id":null,"name":"Formula1.com"},"author":null,"title":"Formula 1 article",
     "description":"A description","url":"url","urlToImage":"imageUrl","publishedAt":"2018-01-31T13:16:33Z"}"""
  private val article2 = """
    {"source":{"id":"cnbc","name":"CNBC"},"author":null,"title":"CNBC Article",
     "description":"A description","url":"url","urlToImage":"imageUrl","publishedAt":"2018-01-31T12:16:33Z"}"""

  private val formulaOneSource = Source(None, "Formula1.com")
  private val expectedArticle1 = Article("Formula 1 article", formulaOneSource, None, "A description", "2018-01-31T13:16:33Z", "url", "imageUrl")
  private val cnbcSource = Source(Some("cnbc"), "CNBC")
  private val expectedArticle2 = Article("CNBC Article", cnbcSource, None, "A description", "2018-01-31T12:16:33Z", "url", "imageUrl")

  it("fails when the provided key is invalid") {
    val path = "/v2/everything"
    val invalidApiKey = "123"
    stubFor(get(urlPathEqualTo(path))
      .withQueryParam(ApiKey, equalTo(invalidApiKey))
      .willReturn(
        aResponse()
          .withStatus(401)))
    val client = new NewsApiClient(invalidApiKey, Host, useHttps = false)
    val Left(errorMessage) = client.everything()
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
      val Right(response) = client.everything()
      response.status shouldBe "ok"
      response.totalResults shouldBe 3
      response.articles shouldBe Seq(expectedArticle1, expectedArticle2)
    }

    it("finds only CNBC articles") {
      val body = s"""{"status":"ok","totalResults":1,"articles":[$article2]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey, Sources -> "cnbc"), body)
      val Right(response) = client.everything(sources = Seq("cnbc"))
      response.status shouldBe "ok"
      response.totalResults shouldBe 1
      response.articles shouldBe Seq(expectedArticle2)
    }

    it("finds articles before a date") {
      val body = s"""{"status":"ok","totalResults":1,"articles":[$article1]}"""
      successfulStub(everythingPath, Seq(ApiKey -> validApiKey, To -> "2018-01-31T13:15:33Z"), body)
      val Right(response) = client.everything(to = Some("2018-01-31T13:15:33Z"))
      response.status shouldBe "ok"
      response.totalResults shouldBe 1
      response.articles shouldBe Seq(expectedArticle1)
    }
  }

  private def successfulStub(path: String, queryParams: Seq[(String, String)], body: String): Unit = {
    val builder = get(urlPathEqualTo(path))
    val withParams = queryParams.foldLeft(builder) { case (b, (k, v)) => b.withQueryParam(k, equalTo(v)) }
    stubFor(withParams.willReturn(aResponse().withStatus(200).withBody(body)))
  }
}
