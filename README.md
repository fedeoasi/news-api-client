# News API Client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fedeoasi/news-api-client_2.12/badge.svg)](
https://maven-badges.herokuapp.com/maven-central/com.github.fedeoasi/news-api-client_2.12)
[![Build Status](https://travis-ci.org/fedeoasi/news-api-client.svg?branch=master)](https://travis-ci.org/fedeoasi/news-api-client)

An HTTP client written in Scala for the [News API](https://newsapi.org).

## Setup

Add the following line to your SBT build definition:

```scala
libraryDependencies += "com.github.fedeoasi" %% "news-api-client" % "0.1"
```

You can get an API key by registering at [News API](https://newsapi.org/account).

```scala
import com.github.fedeoasi.newsapi._
val client = new NewsApiClient("<NEWS_API_KEY>")
```

## Usage

All responses returned by the client are wrapped in an `Either` object. You can
retrieve the response using pattern matching as follows:

```scala
client.topHeadlines() match {
  case Right(response) => //do something with response
  case Left(message) => //Something went wrong
}
```

### Top Headlines

Provides live top and breaking headlines as described in its
[New API documentation](https://newsapi.org/docs/endpoints/top-headlines).

The following code fragment fetches all headlines from US news sources:

```scala
import com.neovisionaries.i18n.CountryCode
client.topHeadlines(country = Some(CountryCode.US))
```

See all the supported parameters [here](
https://github.com/fedeoasi/news-api-client/blob/1d37337205dacb2b5d6246a605b8a22bc1b2c0fa/src/main/scala/com/github/fedeoasi/newsapi/NewsApiClient.scala#L17).

### Everything

Search through millions of articles as described in its
[New API documentation](https://newsapi.org/docs/endpoints/everything)

You can find all articles about BitCoin from the Wall Street Journal as
follows:

```scala
client.everything(
  query = Some("bitcoin"),
  sources = Seq("the-wall-street-journal")
)
```

See all the supported parameters [here](
https://github.com/fedeoasi/news-api-client/blob/1d37337205dacb2b5d6246a605b8a22bc1b2c0fa/src/main/scala/com/github/fedeoasi/newsapi/NewsApiClient.scala#L38).