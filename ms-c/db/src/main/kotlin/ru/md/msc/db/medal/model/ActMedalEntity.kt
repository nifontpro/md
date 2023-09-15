package ru.md.msc.db.medal.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "act_medal", schema = "rew", catalog = "medalist")
class ActMedalEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "act_id")
	var actId: Long = 0

	@Basic
	@Column(name = "medal_id")
	var medalId: Long = 0

	@Basic
	@Column(name = "count")
	var count = 0
	override fun equals(o: Any?): Boolean {
		if (this === o) return true
		if (o == null || javaClass != o.javaClass) return false
		val that = o as ActMedalEntity
		return id == that.id && actId == that.actId && medalId == that.medalId && count == that.count
	}

	override fun hashCode(): Int {
		return Objects.hash(id, actId, medalId, count)
	}
}
