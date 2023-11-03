package ru.md.shop.db.product.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.shop.db.product.model.ProductEntity

@Repository
interface ProductRepo : JpaRepository<ProductEntity, Long> {

	@Modifying
	@Query("delete from ProductEntity p where p.id = :id")
	override fun deleteById(id: Long)

	@Query(
		"""
		from ProductEntity p where p.deptId = :deptId and
		(:maxPrice is null or p.price <= :maxPrice) and
		(:available = false  or p.count > 0) and
		(:filter is null or (upper(p.name) like :filter))
	"""
	)
	fun findByCompany(
		deptId: Long,
		maxPrice: Int? = null,
		available: Boolean = false,
		filter: String? = null,
		pageable: Pageable,
	): Page<ProductEntity>

}