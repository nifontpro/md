package ru.md.msc.db.user.model.gender

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "male_lastname", schema = "gender", catalog = "medalist")
class MaleLastnameEntity(

	@Id
	@Column(name = "lastname")
	val lastname: String

) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as MaleLastnameEntity
		return lastname == that.lastname
	}

	override fun hashCode(): Int {
		return Objects.hash(lastname)
	}
}
