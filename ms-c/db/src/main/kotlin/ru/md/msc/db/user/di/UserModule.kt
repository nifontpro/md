package ru.md.msc.db.user.di

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.db.user.repo.UserDetailsRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.db.user.service.UserServiceImpl
import ru.md.msc.domain.user.service.UserService

@Configuration
class UserModule {
	@Bean
	fun userService(
		userRepository: UserRepository,
		userDetailsRepository: UserDetailsRepository,
		roleRepository: RoleRepository,
		deptDetailsRepository: DeptDetailsRepository
	): UserService = UserServiceImpl(
		userRepository = userRepository,
		userDetailsRepository = userDetailsRepository,
		roleRepository = roleRepository,
		deptDetailsRepository = deptDetailsRepository
	)
}