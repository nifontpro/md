package ru.nb.msgateway

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory
import org.springframework.boot.web.reactive.server.ReactiveWebServerFactory
import org.springframework.boot.web.server.WebServer
import org.springframework.context.annotation.Configuration
import org.springframework.http.server.reactive.HttpHandler


@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
	runApplication<GatewayApplication>(*args)
}

// Добавление http для внутренних запросов
@Configuration
class HttpToHttpsRedirectConfig {
	@Autowired
	var httpHandler: HttpHandler? = null
	var http: WebServer? = null

	@PostConstruct
	fun start() {
		val factory: ReactiveWebServerFactory = NettyReactiveWebServerFactory(8777)
		http = factory.getWebServer(httpHandler).apply {
			start()
		}
	}

	@PreDestroy
	fun stop() {
		http?.stop()
	}
}
