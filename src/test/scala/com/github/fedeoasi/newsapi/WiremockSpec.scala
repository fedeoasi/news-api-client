package com.github.fedeoasi.newsapi

import java.net.ServerSocket

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import org.scalatest.{BeforeAndAfterAll, Suite}

trait WiremockSpec extends BeforeAndAfterAll { this: Suite =>
  protected val Port = {
    val socket = new ServerSocket(0)
    val port = socket.getLocalPort
    socket.close()
    println(s"Using port $port")
    port
  }

  protected val Host = s"localhost:$Port"

  protected val wireMockServer = {
    println("Initializing wiremock")
    val _wireMockServer = new WireMockServer(wireMockConfig().port(Port))
    _wireMockServer.start()
    WireMock.configureFor(Host, Port)
    _wireMockServer
  }

  override protected def afterAll(): Unit = {
    super.afterAll()
    wireMockServer.stop()
  }
}
