package ru.md.msc.db.user

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.user.repo.UserImageRepository
import ru.md.msc.domain.user.service.UserService

@SpringBootTest
class UserTest(
	@Autowired private val userService: UserService,
	@Autowired private val userImageRepository: UserImageRepository
) {

	@Test
	fun findByIdDetails() {
		val data = userService.findByIdDetails(65)
		println("User: $data")
	}

	@Test
	fun findImage() {
		val data = userImageRepository.findByIdAndUserId(imageId = 16, userId = 130)
		println("Image: $data")

	}

}