package ru.md.shop.db.product.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.shop.db.product.model.ProductImageEntity

@Repository
interface ProductImageRepository : JpaRepository<ProductImageEntity, Long> {

	@Query("from ProductImageEntity i where i.id = :imageId and i.productId = :productId")
	fun findByIdAndProductId(imageId: Long, productId: Long): ProductImageEntity?

}