package ru.md.base_rest.utils

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthData(
	val sub: String = "",
	@JsonProperty("email_verified") val emailVerified: Boolean = false,
	@JsonProperty("given_name") val givenName: String = "",
	@JsonProperty("family_name") val familyName: String = "",
	val name: String = "",
	val email: String = "",
)
