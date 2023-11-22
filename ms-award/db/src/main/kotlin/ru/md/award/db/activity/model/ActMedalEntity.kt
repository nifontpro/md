package ru.md.award.db.activity.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "act_medal", schema = "rew", catalog = "medalist")
class ActMedalEntity(

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0,

	@Column(name = "act_id")
	var actId: Long = 0,

	@Column(name = "medal_id")
	var medalId: Long = 0,

	var count: Int = 0,

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as ActMedalEntity
		return id == that.id && actId == that.actId && medalId == that.medalId && count == that.count
	}

	override fun hashCode(): Int {
		return Objects.hash(id, actId, medalId, count)
	}

}
