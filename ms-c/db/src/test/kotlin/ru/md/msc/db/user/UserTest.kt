package ru.md.msc.db.user

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.msc.db.user.repo.UserDetailsRepository
import ru.md.msc.db.user.repo.UserImageRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.user.service.UserService

@SpringBootTest
@Transactional
class UserTest(
	@Autowired private val userService: UserService,
	@Autowired private val userRepository: UserRepository,
	@Autowired private val userImageRepository: UserImageRepository,
	@Autowired private val userDetailsRepository: UserDetailsRepository
) {

	@Test
	fun findByIdDetails() {
		val data = userService.findByIdDetails(65)
		println("User: $data")
	}

	@Test
	@Transactional
	fun update() {
		val userDetails = userDetailsRepository.findByUserId(165) ?: return
		userDetails.user?.firstname = "Test update"
		userDetailsRepository.save(userDetails)
		println("User: $userDetails")
	}

	@Test
	fun findImage() {
		val data = userImageRepository.findByIdAndUserId(imageId = 16, userId = 130)
		println("Image: $data")

	}

	@Test
	fun genderCount() {
		val count = userRepository.genderCount(deptsIds = listOf(81, 87))
		println(count)
	}

	@Test
	fun getUserActivity() {
		val users = userRepository.findByDeptIdIn(deptsIds = listOf(79, 81))
		println(users)
		println(users.count())
		users.forEach {
			it.activities.forEach {activity ->
				println(activity.award)
			}
		}
	}

	@Test
	fun getUserAwards() {
		val users = userRepository.findByDeptIdIn(deptsIds = listOf(79, 81))
		println(users)
		println(users.count())
		users.forEach {
			it.awards.forEach { awardEntity ->
				println(awardEntity)
			}
		}
	}
}