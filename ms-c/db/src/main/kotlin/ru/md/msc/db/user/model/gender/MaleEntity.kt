package ru.md.msc.db.user.model.gender

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "male", schema = "gender", catalog = "medalist")
class MaleEntity(

	@Id
	@Column(name = "name")
	val name: String

) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as MaleEntity
		return name == that.name
	}

	override fun hashCode(): Int {
		return Objects.hash(name)
	}
}
