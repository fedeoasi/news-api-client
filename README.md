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

[Top Headlines](https://newsapi.org/docs/endpoints/top-headlines) endpoint.


Fetch the headlines for US newspapers:

```scala
import com.neovisionaries.i18n.CountryCode
client.topHeadlines(country = Some(CountryCode.US))
```

### Everything

[Everything](https://newsapi.org/docs/endpoints/everything) endpoint.


Fetch all articles about BitCoin from the Wall Street Journal:

```scala
client.everything(
  query = Some("bitcoin"),
  sources = Seq("the-wall-street-journal")
)
