package ru.md.msc.rest.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SpringSecurityFluxConfig {

	@Bean
	fun springSecurityFilterChain(
		http: ServerHttpSecurity,
		jwtAuthenticationConverter: Converter<Jwt, Mono<AbstractAuthenticationToken>>
	): SecurityWebFilterChain {

		http.authorizeExchange()
			.pathMatchers("/user/test").permitAll()
			.pathMatchers("/user/**").hasRole("user")
			.pathMatchers("/admin/**").hasRole("admin")
			.anyExchange().authenticated()
			.and()
			.csrf().disable()
//			.cors()
//			.and()
			.oauth2ResourceServer()
			.jwt()
			.jwtAuthenticationConverter(jwtAuthenticationConverter)
//			.and()
//			.authenticationEntryPoint(OAuth2FluxExceptionHandler())

		return http.build()
	}

//	@Bean
//	fun corsConfigurationSource(): CorsWebFilter {
//		val configuration = CorsConfiguration().apply {
//			allowedOrigins = listOf("*")
//			allowedHeaders = listOf("*")
//			allowedMethods = listOf("*")
//		}
//		val source = UrlBasedCorsConfigurationSource().apply {
//			registerCorsConfiguration("/**", configuration)
//		}
//		return CorsWebFilter(source)
//	}

}