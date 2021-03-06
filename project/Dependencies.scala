import sbt._

object Dependencies {
  lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.5.3"
  lazy val nvI18n = "com.neovisionaries" % "nv-i18n" % "1.22"
  lazy val scalajHttp = "org.scalaj" %% "scalaj-http" % "2.3.0"
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test
  lazy val wireMock = "com.github.tomakehurst" % "wiremock" % "2.15.0" % Test
  lazy val mockito = "org.mockito" % "mockito-core" % "2.16.0" % Test
}
