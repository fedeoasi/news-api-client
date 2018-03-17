# News API Client

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.fedeoasi/news-api-client_2.12/badge.svg)](
https://maven-badges.herokuapp.com/maven-central/com.github.fedeoasi/news-api-client_2.12)
[![Build Status](https://travis-ci.org/fedeoasi/news-api-client.svg?branch=master)](https://travis-ci.org/fedeoasi/news-api-client)

An HTTP client written in Scala for the [News API](https://newsapi.org).

## Setup

Add the following line to your SBT build definition:

```scala
libraryDependencies += "com.github.fedeoasi" %% "news-api-client" % "0.4"
```

You can get an API key by registering at [News API](https://newsapi.org/account).

Both a synchronous and an asynchronous version of the client are provided.

Synchronous client:

```scala
import com.github.fedeoasi.newsapi._
val client = NewsApiClient("<NEWS_API_KEY>")
```

Asynchronous client:

```scala
import com.github.fedeoasi.newsapi._
import scala.concurrent.ExecutionContext.Implicits.global
val client = AsyncNewsApiClient("<NEWS_API_KEY>")
```

The asynchronous client requires an `ExecutionContext` at construction
time. You can choose whether you want to provide your own execution
context or bring the default one into scope.

## Usage

All responses returned by the client are wrapped in an `Either` object. You can
retrieve the response using pattern matching as follows:

```scala
client.topHeadlines() match {
  case Right(response) => //do something with response
  case Left(message) => //Something went wrong
}
```

The asynchronous client wraps the above result type into a scala `Future`.
Other than that the method names and parameters are identical.

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

## Examples

- Synchronous client [example](https://github.com/fedeoasi/news-api-client/blob/master/src/main/scala/com/github/fedeoasi/newsapi/SampleMain.scala)
- Asynchronous client [example](https://github.com/fedeoasi/news-api-client/blob/master/src/main/scala/com/github/fedeoasi/newsapi/SampleAsyncMain.scala)