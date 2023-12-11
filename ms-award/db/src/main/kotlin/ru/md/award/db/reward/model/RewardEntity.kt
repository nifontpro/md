package ru.md.award.db.reward.model

import jakarta.persistence.*
import ru.md.award.db.medal.model.MedalObjEntity
import ru.md.base_db.user.model.UserEntity
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "act", schema = "rew", catalog = "medalist")
class RewardEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "start_date")
	var date: LocalDateTime,

	@JoinColumn(name = "medal_obj_id")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	var medalObjEntity: MedalObjEntity,

	@JoinColumn(name = "user_id")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	var userEntity: UserEntity,
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val reward = other as RewardEntity
		return id == reward.id && medalObjEntity.id == reward.medalObjEntity.id && userEntity.id == reward.userEntity.id
	}

	override fun hashCode(): Int {
		return Objects.hash(id, medalObjEntity.id, userEntity.id)
	}
}
