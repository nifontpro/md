package ru.md.msc.db.award.model

import jakarta.persistence.*
import ru.md.msc.db.user.model.UserEntity
import ru.md.msc.domain.award.model.ActionType
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "activity", schema = "md", catalog = "medalist")
class ActivityEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	var date: LocalDateTime? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	var user: UserEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "award_id")
	var award: AwardEntity? = null,

	@Column(name = "action_code")
	var actionType: ActionType = ActionType.UNDEF,

	@Column(name = "is_activ")
	var activ: Boolean = true,

	@Column(name = "dept_id")
	var deptId: Long = 0,

	@Column(name = "auth_id")
	var authId: Long = 0,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val activityEntity = other as ActivityEntity
		return id == activityEntity.id && date == activityEntity.date && actionType == activityEntity.actionType
				&& activ == activityEntity.activ && deptId == activityEntity.deptId && authId == activityEntity.authId
	}

	override fun hashCode(): Int {
		return Objects.hash(id, date, actionType, activ, deptId, authId)
	}
}
