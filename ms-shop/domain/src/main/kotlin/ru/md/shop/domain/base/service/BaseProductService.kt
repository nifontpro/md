package ru.md.shop.domain.base.service

interface BaseProductService {
	fun findDeptIdByProductId(productId: Long): Long
}