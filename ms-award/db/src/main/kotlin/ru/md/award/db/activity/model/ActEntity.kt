package ru.md.award.db.activity.model

import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import ru.md.award.db.medal.model.MedalObjEntity
import ru.md.award.domain.activity.model.ActClass
import ru.md.base_db.user.model.UserEntity
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "act", schema = "rew", catalog = "medalist")
class ActEntity(
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "name")
	var name: String = "",

	@Column(name = "class_code")
	var actClass: ActClass = ActClass.UNDEF,

	var description: String? = null,

	@Column(name = "start_date")
	var startDate: LocalDateTime? = null,

	@Column(name = "result_date")
	var resultDate: LocalDateTime? = null,

	@Column(name = "end_date")
	var endDate: LocalDateTime? = null,

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "act_id")
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("name")
	val medals: List<MedalObjEntity> = emptyList(),

	@Fetch(FetchMode.SUBSELECT)
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		schema = "rew", name = "act_user",
		joinColumns = [JoinColumn(name = "act_id")],
		inverseJoinColumns = [JoinColumn(name = "user_id")]
	)
	val users: List<UserEntity> = emptyList(),

	) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val actEntity = other as ActEntity
		return id == actEntity.id && name == actEntity.name && actClass == actEntity.actClass && description == actEntity.description && startDate == actEntity.startDate && resultDate == actEntity.resultDate && endDate == actEntity.endDate
	}

	override fun hashCode(): Int {
		return Objects.hash(id, name, actClass, description, startDate, resultDate, endDate)
	}
}
