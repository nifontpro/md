package ru.md.msc.rest.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import ru.md.base_rest.security.KCRoleConverter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SpringSecurityConfig {

	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {

		// конвертер для настройки spring security
		val jwtAuthenticationConverter = JwtAuthenticationConverter()
		// подключаем конвертер ролей
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(KCRoleConverter())

		http
			.csrf { csrf -> csrf.disable() }
//			.cors {}// Разрешает запросы типа OPTIONS
			.authorizeHttpRequests { auth ->
				auth.requestMatchers("/award/get_item/**").permitAll()
				auth.requestMatchers("/user/test/**").permitAll()
				auth.requestMatchers("/user/admin/gender/**").permitAll()
				auth.anyRequest().hasRole("user")
			}
			.oauth2ResourceServer { oauth2ResourceServer ->
				oauth2ResourceServer
					.jwt { jwt ->
						jwt.jwtAuthenticationConverter(jwtAuthenticationConverter)
					}
			}
//			.authenticationEntryPoint(OAuth2ExceptionHandler())

		return http.build()
	}

//	@Bean
//	fun corsConfigurationSource(): CorsConfigurationSource {
//		val configuration = CorsConfiguration() // .applyPermitDefaultValues()
//			.apply {
//				allowedOrigins = listOf("*")
//				allowedHeaders = listOf("*")
//				allowedMethods = listOf("*")
//			}
//		val source = UrlBasedCorsConfigurationSource().apply {
//			registerCorsConfiguration("/**", configuration)
//		}
//		return source
//	}
}