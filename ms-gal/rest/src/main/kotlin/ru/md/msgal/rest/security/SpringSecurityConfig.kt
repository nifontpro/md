package ru.md.msgal.rest.security

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

		val jwtAuthenticationConverter = JwtAuthenticationConverter()
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(KCRoleConverter())

		http.authorizeHttpRequests()
			.requestMatchers("/item/get_id").hasAnyRole("micro", "user")
			.requestMatchers("/item/admin").hasAnyRole("admin")
			.requestMatchers("/folder/admin").hasAnyRole("admin")
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