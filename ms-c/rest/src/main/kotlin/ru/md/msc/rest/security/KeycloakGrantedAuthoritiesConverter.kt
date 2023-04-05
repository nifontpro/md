package ru.md.msc.rest.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt


class KeycloakGrantedAuthoritiesConverter : Converter<Jwt, Collection<GrantedAuthority>> {

	override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
		val realmRoles = realmRoles(jwt)

		val returnValue: MutableCollection<GrantedAuthority> = mutableListOf()
		for (roleName in realmRoles) {
			returnValue.add(SimpleGrantedAuthority("ROLE_$roleName"))
		}
		return returnValue
	}

	@Suppress("UNCHECKED_CAST")
	private fun realmRoles(jwt: Jwt): List<String> {
		val realmAccess = jwt.claims[CLAIM_REALM_ACCESS] as Map<String, Any>?
		if (realmAccess.isNullOrEmpty()) {
			return emptyList() // пустая коллекция - нет ролей
		}
		return realmAccess[ROLES] as List<String>
	}

	companion object {
		private const val ROLES = "roles"
		private const val CLAIM_REALM_ACCESS = "realm_access"
	}
}