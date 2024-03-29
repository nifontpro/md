package ru.md.msc.db.award.model

import jakarta.persistence.*
import ru.md.base_db.user.model.UserEntity
import ru.md.msc.domain.award.model.ActionType
import java.time.LocalDateTime
import java.util.*

@NamedEntityGraph(
	name = "activityWithAward",
	attributeNodes = [NamedAttributeNode("award")]
)

//@NamedEntityGraph(
//	name = "activityWithDept",
//	attributeNodes = [NamedAttributeNode("dept")]
//)

@NamedEntityGraph(
	name = "activityWithUserWithDept",
	attributeNodes = [
		NamedAttributeNode(value = "user", subgraph = "userWithDeptSub"),
	],
	subgraphs = [NamedSubgraph(name = "userWithDeptSub", attributeNodes = [NamedAttributeNode("dept")])]
)

@NamedEntityGraph(
	name = "activityWithUser",
	attributeNodes = [NamedAttributeNode("user")]
)

@NamedEntityGraph(
	name = "activityWithUserAndAward",
	attributeNodes = [NamedAttributeNode("user"), NamedAttributeNode("award")]
)

@NamedEntityGraph(
	name = "activityWithUserAndAwardAndDept",
	attributeNodes = [
		NamedAttributeNode("user", subgraph = "userWithDeptSub"),
		NamedAttributeNode("award"),
	],
	subgraphs = [NamedSubgraph(name = "userWithDeptSub", attributeNodes = [NamedAttributeNode("dept")])]
)

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

	@Column(name = "auth_id")
	var authId: Long? = null,

	@Column(name = "award_score")
	var awardScore: Int = 0,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val activityEntity = other as ActivityEntity
		return id == activityEntity.id && date == activityEntity.date && actionType == activityEntity.actionType
	}

	override fun hashCode(): Int {
		return Objects.hash(id, date, actionType)
	}

	override fun toString(): String {
		return "{Activity(id: $id, userId: ${user?.id}, awardId:${award?.id}, date: $date, actionType: $actionType, activ: $activ)}\n"
	}
}
