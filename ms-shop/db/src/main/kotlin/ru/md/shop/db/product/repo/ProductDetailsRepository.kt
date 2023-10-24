package ru.md.shop.db.product.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.shop.db.product.model.ProductDetailsEntity

@Repository
interface ProductDetailsRepository : JpaRepository<ProductDetailsEntity, Long>