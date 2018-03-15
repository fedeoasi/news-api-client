# News API Client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fedeoasi/news-api-client_2.12/badge.svg)](
https://maven-badges.herokuapp.com/maven-central/com.github.fedeoasi/news-api-client_2.12)
[![Build Status](https://travis-ci.org/fedeoasi/news-api-client.svg?branch=master)](https://travis-ci.org/fedeoasi/news-api-client)

An HTTP client written in Scala for the [News API](https://newsapi.org).

## Setup

Add the following line to your SBT build definition:

```scala
libraryDependencies += "com.github.fedeoasi" %% "news-api-client" % "0.2"
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
[News API documentation](https://newsapi.org/docs/endpoints/top-headlines).

The following code fragment fetches all headlines from US news sources:

```scala
import com.neovisionaries.i18n.CountryCode
client.topHeadlines(country = Some(CountryCode.US))
```

See all the supported parameters [here](
https://github.com/fedeoasi/news-api-client/blob/7040e778697c25a1a5073701e3b4af0125b549ef/src/main/scala/com/github/fedeoasi/newsapi/NewsApiClient.scala#L18).

### Everything

Search through millions of articles as described in its
[News API documentation](https://newsapi.org/docs/endpoints/everything).

You can find all articles about BitCoin from the Wall Street Journal as
follows:

```scala
client.everything(
  query = Some("bitcoin"),
  sources = Seq("the-wall-street-journal")
)
```

See all the supported parameters [here](
https://github.com/fedeoasi/news-api-client/blob/7040e778697c25a1a5073701e3b4af0125b549ef/src/main/scala/com/github/fedeoasi/newsapi/NewsApiClient.scala#L39).

### Sources

Returns a subset of the news publishers that top headlines are available
from as described in its
[News API documentation](https://newsapi.org/docs/endpoints/sources).

You can find all sources as follows:

```scala
client.sources()
```

See all the supported parameters [here](
https://github.com/fedeoasi/news-api-client/blob/7040e778697c25a1a5073701e3b4af0125b549ef/src/main/scala/com/github/fedeoasi/newsapi/NewsApiClient.scala#L67).

## Coming Soon
- Support for asynchronous HTTP calls using Futures