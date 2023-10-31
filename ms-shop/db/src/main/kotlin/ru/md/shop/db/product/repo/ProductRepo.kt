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

	@Query("select p.deptId from ProductEntity p where p.id = :productId")
	fun findDeptId(productId: Long): Long?

	fun findByDeptId(deptId: Long, pageable: Pageable): Page<ProductEntity>

}