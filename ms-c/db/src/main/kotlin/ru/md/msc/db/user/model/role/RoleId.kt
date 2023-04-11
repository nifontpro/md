package ru.md.msc.db.user.model.role
//
//import jakarta.persistence.Column
//import java.io.Serializable
//import java.util.*
//
//class RoleId(
//
//	@Column(name = "user_id")
//	val userId: Long = 0,
//	val code: String = ""
//) : Serializable {
//
//	override fun equals(other: Any?): Boolean {
//		if (this === other) return true
//		if (other == null || javaClass != other.javaClass) return false
//		val roleId = other as RoleId
//		return userId == roleId.userId && code == roleId.code
//	}
//
//	override fun hashCode(): Int {
//		return Objects.hash(code + userId)
//	}
//}