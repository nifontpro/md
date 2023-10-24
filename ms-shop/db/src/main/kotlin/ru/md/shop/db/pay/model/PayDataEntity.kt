package ru.md.shop.db.pay.model

import jakarta.persistence.*
import java.sql.Timestamp
import java.util.*

@Entity
@Table(name = "pay_data", schema = "shop", catalog = "medalist")
class PayDataEntity {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	var id: Long = 0

	@Basic
	@Column(name = "date_op")
	var dateOp: Timestamp? = null

	@Basic
	@Column(name = "user_id")
	var userId: Long = 0

	@Basic
	@Column(name = "price")
	var price = 0
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
