import Dependencies._

inThisBuild(Seq(
  organization := "com.github.fedeoasi",
  pomIncludeRepository := { _ => false },
  licenses += ("MIT License", url("http://www.opensource.org/licenses/mit-license.php")),
  homepage := Some(url("https://github.com/fedeoasi/news-api-client")),
  scmInfo := Some(ScmInfo(url("https://github.com/fedeoasi/news-api-client"), "scm:git@github.com:fedeoasi/news-api-client.git")),
  developers := List(Developer("fedeoasi", "Federico Caimi", "fedeoasi@gmail.com", url("http://www.github.com/fedeoasi"))),
  publishArtifact in Test := false,
  publishMavenStyle := true,
  crossScalaVersions := Seq("2.12.4", "2.11.12"),
  scalaVersion := crossScalaVersions.value.head
))

lazy val root = (project in file(".")).
  settings(
    name := "News API Client",
    libraryDependencies ++= Seq(
      json4s,
      mockito,
      nvI18n,
      scalajHttp,
      scalaTest,
      wireMock
    ),
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    initialCommands in console := """
      import com.github.fedeoasi.newsapi._
      import com.neovisionaries.i18n._
    """
  )
