package ru.md.msc.rest.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

// класс конвертер из данных JWT в роли spring security
@Suppress("unused")
class KCRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {

	@Suppress("UNCHECKED_CAST")
	override fun convert(jwt: Jwt): Collection<GrantedAuthority> {

		// получаем доступ к разделу JSON
		val realmAccess = jwt.claims["realm_access"] as Map<String, Any>?

		// если раздел JSON не будет найден - значит нет ролей
		if (realmAccess.isNullOrEmpty()) {
			return emptyList() // пустая коллекция - нет ролей
		}

		val returnValue: MutableCollection<GrantedAuthority> = mutableListOf()
		for (roleName in (realmAccess["roles"] as List<String>) // проходим по всем значениям из JSON
		) {
			// создаем объект SimpleGrantedAuthority - это дефолтная реализация GrantedAuthority
			returnValue.add(SimpleGrantedAuthority("ROLE_$roleName")) // префикс ROLE обязателен
		}
		return returnValue
	}

}