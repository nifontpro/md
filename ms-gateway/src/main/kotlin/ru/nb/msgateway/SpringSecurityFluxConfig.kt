package ru.nb.msgateway

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SpringSecurityFluxConfig {

	@Bean
	fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {

		http.authorizeExchange()
			.anyExchange().permitAll()
			.and()
			.csrf().disable()
			.cors()

		return http.build()
	}

	@Bean
	fun corsConfigurationSource(): CorsWebFilter {
		val configuration = CorsConfiguration().apply {
			allowedOrigins = listOf("*")
//			allowedOrigins = listOf("localhost")
			allowedHeaders = listOf("*")
			allowedMethods = listOf("*")
		}
		val source = UrlBasedCorsConfigurationSource().apply {
			registerCorsConfiguration("/**", configuration)
		}
		return CorsWebFilter(source)
	}

}