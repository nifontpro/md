package ru.md.msc.rest.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

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

		http.authorizeHttpRequests()
			.requestMatchers("/user/test").permitAll()
			.requestMatchers("/user/**").hasRole("user")
			.requestMatchers("/admin/**").hasRole("admin")
			.anyRequest().hasRole("user")
			.and() // добавляем новые настройки, не связанные с предыдущими
			.csrf().disable()
//			.cors()// Разрешает запросы типа OPTIONS
//			.and()
			.oauth2ResourceServer()// добавляем конвертер ролей из JWT в Authority (Role)
			.jwt()
			.jwtAuthenticationConverter(jwtAuthenticationConverter)
			.and()
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