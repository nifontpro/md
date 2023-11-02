package ru.md.shop.db.pay.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.shop.db.pay.model.PayDataEntity
import ru.md.shop.domain.pay.model.PayCode
import java.time.LocalDateTime

@Repository
interface PayDataRepo : JpaRepository<PayDataEntity, Long> {

	@EntityGraph("payDataWithProduct")
	@Query(
		"""
		from PayDataEntity p where p.productEntity.deptId = :deptId and
		(:userIdNotPresent = true or p.userEntity.id = :userId) and 
		(:isActive is null or p.isActive = :isActive) and
		(:payCode is null or p.payCode = :payCode) and
		(coalesce(:minDate, null) is null or p.dateOp >= :minDate) and 
		(coalesce(:maxDate, null) is null or p.dateOp <= :maxDate) and
		((:filter is null) or (upper(p.productEntity.name) like :filter))
	"""
	)
	fun findPaysDataByCompany(
		deptId: Long,
		userId: Long,
		userIdNotPresent: Boolean,
		isActive: Boolean? = null,
		payCode: PayCode? = null,
		minDate: LocalDateTime? = null,
		maxDate: LocalDateTime? = null,
		filter: String? = null,
		pageable: Pageable,
	): Page<PayDataEntity>

}