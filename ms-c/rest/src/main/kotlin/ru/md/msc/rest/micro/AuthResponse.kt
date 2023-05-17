package ru.md.msc.rest.micro

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties
data class AuthResponse(

	@JsonProperty("access_token")
	val accessToken: String,

	@JsonProperty("expires_in")
	val expiresIn: Long,

	@JsonProperty("token_type")
	val tokenType: String,

	val scope: String,
)
