package com.github.fedeoasi.newsapi

import com.github.fedeoasi.newsapi.NewsApiClient.Params._
import com.github.tomakehurst.wiremock.client.WireMock._
import org.scalatest.{FunSpec, Matchers}

class NewsApiClientTest extends FunSpec with Matchers with WiremockSpec {
  private val validApiKey = "validApiSecret"
  private val path = "/v2/everything"

  it("fails when the provided key is invalid") {
    val invalidApiKey = "123"
    stubFor(get(urlPathEqualTo(path))
      .withQueryParam(ApiKey, equalTo(invalidApiKey))
      .willReturn(
        aResponse()
          .withStatus(401)))
    val client = new NewsApiClient(invalidApiKey, Host)
    val Left(errorMessage) = client.everything()
    errorMessage should include("401")
  }

  describe("Everything") {
    it("finds articles about formula 1") {
      stubFor(get(urlPathEqualTo(path))
        .withQueryParam(ApiKey, equalTo(validApiKey))
        .willReturn(
          aResponse()
            .withStatus(200)
            .withBody(
              """
                |{"status":"ok","totalResults":1,"articles":[
                |  {"source":{"id":null,"name":"Formula1.com"},"author":null,"title":"Formula 1 article",
                |   "description":"A description","url":"url","urlToImage":"imageUrl","publishedAt":"2018-01-31T13:16:33Z"}
                |]}""".stripMargin)))
      val client = new NewsApiClient(validApiKey, Host)
      val Right(response) = client.everything()
      response.status shouldBe "ok"
      response.totalResults shouldBe 1
      val source = Source(None, "Formula1.com")
      response.articles shouldBe Seq(Article("Formula 1 article", source, None, "A description", "2018-01-31T13:16:33Z", "url", "imageUrl"))
    }
  }
}
