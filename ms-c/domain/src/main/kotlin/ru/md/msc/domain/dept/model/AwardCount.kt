package ru.md.msc.domain.dept.model

data class AwardCount(
	val deptId: Long = 0,
	val deptName: String = "",
	val awardCount: Long = 0,
	val nomineeCount: Long = 0,
)

interface IAwardCount {
	fun getDeptId(): Long
	fun getDeptName(): String
	fun getAwardCount(): Long
	fun getNomineeCount(): Long

}
