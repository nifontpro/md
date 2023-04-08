package ru.md.msc.db.award.model

import jakarta.persistence.*
import ru.md.msc.db.dept.model.DeptEntity
import ru.md.msc.db.user.model.UserEntity
import java.sql.Timestamp
import java.util.*

@Entity
class Activity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	var date: Timestamp? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	var user: UserEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "dept_id")
	var dept: DeptEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "award_id")
	var award: AwardEntity? = null,

	@Basic
	@Column(name = "action_code")
	var actionCode: String? = null,

	@Basic
	@Column(name = "is_activ", nullable = true)
	var activ: Boolean? = null

) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val activity = other as Activity
		return id == activity.id && date == activity.date && actionCode == activity.actionCode && activ == activity.activ
	}

	override fun hashCode(): Int {
		return Objects.hash(id, date, actionCode, activ)
	}
}
