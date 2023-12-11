package ru.md.award.db.medal.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "medal_obj", schema = "rew", catalog = "medalist")
class MedalObjEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "act_id")
	var actId: Long = 0,

	var count: Int = 0,

	var score: Int = 0,

	@JoinColumn(name = "medal_id")
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	var medalEntity: MedalEntity,
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as MedalObjEntity
		return id == that.id && actId == that.actId && medalEntity.id == that.medalEntity.id && count == that.count
	}

	override fun hashCode(): Int {
		return Objects.hash(id, actId, medalEntity.id, count)
	}

}
