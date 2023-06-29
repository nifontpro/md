package ru.md.msc.db.user

import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import ru.md.base_db.mapper.toPageRequest
import ru.md.base_domain.model.BaseOrder
import ru.md.base_domain.model.BaseQuery
import ru.md.base_domain.model.Direction
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.db.user.model.mappers.toUser
import ru.md.msc.db.user.repo.UserDetailsRepository
import ru.md.msc.db.user.repo.UserImageRepository
import ru.md.msc.db.user.repo.UserRepository
import ru.md.msc.domain.user.service.UserService
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class UserTest(
	@Autowired private val userService: UserService,
	@Autowired private val userRepository: UserRepository,
	@Autowired private val userImageRepository: UserImageRepository,
	@Autowired private val userDetailsRepository: UserDetailsRepository,
	@Autowired private val deptRepository: DeptRepository,
) {

	@Test
	fun findByIdDetails() {
		val data = userService.findByIdDetails(65)
		println("User: $data")
	}

	@Test
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
			it.activities.forEach { activity ->
				println(activity.award)
			}
		}
	}

	@Test
	fun getParentId() {
		val parentId = userRepository.findParentDeptId(userId = 123)
		println(parentId)
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

	@Test
	fun setMainImg() {
		val res = userService.setMainImage(130)
		println(res)
	}

	@Test
	fun allActCount() {
		val deptIds = deptRepository.subTreeIds(1)
		val baseQuery = BaseQuery(
			page = 0,
			pageSize = 3,
			orders = listOf(BaseOrder(field = "(awardCount)", Direction.DESC))
		)
		val res = userRepository.findUsersWithAwardCount(
			deptsIds = deptIds,
			minDate = LocalDateTime.of(2023, 5, 10, 0, 0, 0),
//			minDate = Timestamp.valueOf(LocalDateTime.of(2023, 5, 10, 0, 0, 0)),
			pageable = baseQuery.toPageRequest()
		)
		val data = res.content.map { it.toUser() }
		println(data)
	}
}