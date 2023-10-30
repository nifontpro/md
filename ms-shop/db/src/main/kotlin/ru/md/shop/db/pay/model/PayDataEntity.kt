package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "pay_data", schema = "shop", catalog = "medalist")
class PayDataEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long = 0,

	@Column(name = "date_op")
	var dateOp: LocalDateTime? = null,

	@Column(name = "user_id")
	var userId: Long = 0,

	var price: Int = 0,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as PayDataEntity
		return id == that.id && userId == that.userId && price == that.price && dateOp == that.dateOp
	}

	override fun hashCode(): Int {
		return Objects.hash(id, dateOp, userId, price)
	}
}
