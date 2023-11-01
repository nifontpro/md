package ru.md.shop.db.base.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.shop.db.product.model.ProductEntity

@Repository
interface BaseProductRepo : JpaRepository<ProductEntity, Long> {

	@Query("select p.deptId from ProductEntity p where p.id = :productId")
	fun findDeptId(productId: Long): Long?

}