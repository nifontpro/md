package ru.md.base_domain.model

data class PageResult<R>(
	val data: List<R>,
	val pageInfo: PageInfo
)

data class PageInfo(
	val pageSize: Int,
	val pageNumber: Int,
	val numberOfElements: Int,
	val totalPages: Int,
	val totalElements: Long,
)