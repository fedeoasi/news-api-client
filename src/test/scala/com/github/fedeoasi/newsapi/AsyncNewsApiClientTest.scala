package com.github.fedeoasi.newsapi

import java.time.Instant

import com.neovisionaries.i18n.{CountryCode, LanguageCode}
import org.scalatest.{AsyncFunSpec, Matchers}
import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito._

class AsyncNewsApiClientTest extends AsyncFunSpec with Matchers with MockitoSugar {
  describe("Top Headlines") {
    val response = Right(ArticlesResponse(Seq.empty, 0, "ok"))

    it("finds all headlines") {
      val mockClient = mock[NewsApiClient]
      when(mockClient.topHeadlines(None, None, None, Seq.empty, None, None)).thenReturn(response)
      val asyncClient = new AsyncNewsApiClient(mockClient)
      asyncClient.topHeadlines().map { result =>
        result shouldBe response
      }
    }

    it("finds headlines using all parameters") {
      val mockClient = mock[NewsApiClient]
      when(mockClient.topHeadlines(Some("query"), Some(CountryCode.US), Some(Category.business), Seq("cnbc"), Some(3), Some(2))).thenReturn(response)
      val asyncClient = new AsyncNewsApiClient(mockClient)
      asyncClient.topHeadlines(Some("query"), Some(CountryCode.US), Some(Category.business), Seq("cnbc"), Some(3), Some(2)).map { result =>
        result shouldBe response
      }
    }
  }

  describe("Everything") {
    val response = Right(ArticlesResponse(Seq.empty, 0, "ok"))

    it("finds all articles for a query") {
      val mockClient = mock[NewsApiClient]
      when(mockClient.everything("q", Seq.empty, Seq.empty, None, None, None, None, None, None)).thenReturn(response)
      val asyncClient = new AsyncNewsApiClient(mockClient)
      asyncClient.everything("q").map { result =>
        result shouldBe response
      }
    }

    it("finds headlines using all parameters") {
      val mockClient = mock[NewsApiClient]
      val fromAnHourAgo = Instant.now.minusSeconds(3600)
      val toNow = Instant.now
      when(mockClient.everything(
        "q",
        Seq("cnbc"),
        Seq("domain.com"),
        Some(fromAnHourAgo),
        Some(toNow),
        Some(LanguageCode.en),
        Some(SortBy.publishedAt),
        Some(3),
        Some(2))).thenReturn(response)

      val asyncClient = new AsyncNewsApiClient(mockClient)
      asyncClient.everything("q",
        Seq("cnbc"),
        Seq("domain.com"),
        Some(fromAnHourAgo),
        Some(toNow),
        Some(LanguageCode.en),
        Some(SortBy.publishedAt),
        Some(3),
        Some(2)).map { result =>

        result shouldBe response
      }
    }
  }

  describe("Sources") {
    val response = Right(SourcesResponse(Seq.empty, "ok"))

    it("finds all sources") {
      val mockClient = mock[NewsApiClient]
      when(mockClient.sources(None, None, None)).thenReturn(response)
      val asyncClient = new AsyncNewsApiClient(mockClient)
      asyncClient.sources().map { result =>
        result shouldBe response
      }
    }

    it("finds sources using all parameters") {
      val mockClient = mock[NewsApiClient]
      when(mockClient.sources(Some(Category.business), Some(LanguageCode.en), Some(CountryCode.US))).thenReturn(response)
      val asyncClient = new AsyncNewsApiClient(mockClient)
      asyncClient.sources(Some(Category.business), Some(LanguageCode.en), Some(CountryCode.US)).map { result =>
        result shouldBe response
      }
    }
  }
}
