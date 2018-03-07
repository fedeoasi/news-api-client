import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.github.fedeoasi",
      scalaVersion := "2.12.4",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "News API Client",
    libraryDependencies ++= Seq(
      json4s,
      nvI18n,
      scalajHttp,
      scalaTest,
      wireMock
    )
  )
