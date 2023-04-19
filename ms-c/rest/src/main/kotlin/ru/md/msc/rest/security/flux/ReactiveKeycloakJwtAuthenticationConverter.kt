package ru.md.msc.rest.security.flux
//
//import org.springframework.core.convert.converter.Converter
//import org.springframework.security.authentication.AbstractAuthenticationToken
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.oauth2.jwt.Jwt
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
//import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter
//import reactor.core.publisher.Flux
//import reactor.core.publisher.Mono
//
///**
// * @see org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter
// */
//class ReactiveKeycloakJwtAuthenticationConverter(
//	jwtGrantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>>
//) : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
//
//	private val jwtGrantedAuthoritiesConverter: Converter<Jwt, Flux<GrantedAuthority>>
//
//	init {
//		this.jwtGrantedAuthoritiesConverter = ReactiveJwtGrantedAuthoritiesConverterAdapter(
//			jwtGrantedAuthoritiesConverter
//		)
//	}
//
//	override fun convert(jwt: Jwt): Mono<AbstractAuthenticationToken>? {
//		return jwtGrantedAuthoritiesConverter.convert(jwt)
//			?.collectList()
//			?.map { authorities: List<GrantedAuthority> ->
//				JwtAuthenticationToken(jwt, authorities, extractUsername(jwt))
//			}
//	}
//
//	private fun extractUsername(jwt: Jwt): String {
//		return if (jwt.hasClaim(USERNAME_CLAIM)) jwt.getClaimAsString(USERNAME_CLAIM) else jwt.subject
//	}
//
//	companion object {
//		private const val USERNAME_CLAIM = "preferred_username"
//	}
//}

/*
webflux:
https://reddeveloper.ru/questions/iyerarkhiya-rolei-v-spring-webflux-security-J8nAv
https://issues.redhat.com/browse/KEYCLOAK-7644
https://github.com/rbiedrawa/spring-webflux-keycloak-demo/blob/master/src/main/java/com/rbiedrawa/oauth/config/SecurityConfiguration.java
 */