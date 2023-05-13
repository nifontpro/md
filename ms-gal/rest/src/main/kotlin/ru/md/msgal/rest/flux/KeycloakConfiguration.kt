package ru.md.msgal.rest.flux

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import reactor.core.publisher.Mono


@Configuration
class KeycloakConfiguration {

	@Bean
	fun keycloakGrantedAuthoritiesConverter(): Converter<Jwt, Collection<GrantedAuthority>> {
		return KeycloakGrantedAuthoritiesConverter()
	}

	@Bean
	fun keycloakJwtAuthenticationConverter(
		converter: Converter<Jwt, Collection<GrantedAuthority>>
	): Converter<Jwt, Mono<AbstractAuthenticationToken>> {
		return ReactiveKeycloakJwtAuthenticationConverter(converter)
	}
}