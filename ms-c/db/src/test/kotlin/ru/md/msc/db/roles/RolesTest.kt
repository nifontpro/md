package ru.md.msc.db.roles

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.domain.user.model.RoleUser

@SpringBootTest
class RolesTest(
	@Autowired private val roleRepository: RoleRepository
) {

	@Test
	fun getOwnerByEmail() {
		val roleEntity = roleRepository.findByRoleUserAndUserAuthEmail(
			roleUser = RoleUser.OWNER,
			userEmail = "leprinxol@gmail.com"
		)
		println(roleEntity)
	}
}