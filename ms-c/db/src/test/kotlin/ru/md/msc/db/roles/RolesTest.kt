package ru.md.msc.db.roles

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import ru.md.msc.db.user.repo.RoleRepository
import ru.md.msc.domain.user.model.RoleEnum

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RolesTest(
	@Autowired private val roleRepository: RoleRepository
) {

	@Test
	fun getOwnerByEmail() {
		val roleEntity = roleRepository.findByRoleEnumAndUserEmail(
			roleEnum = RoleEnum.OWNER,
			userEmail = "leprinxol@gmail.com"
		)
		println(roleEntity)
	}
}