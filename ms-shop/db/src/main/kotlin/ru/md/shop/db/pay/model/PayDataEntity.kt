package ru.md.shop.db.pay.model

import jakarta.persistence.*
import ru.md.base_db.user.model.UserEntity
import ru.md.shop.db.product.model.ProductEntity
import ru.md.shop.domain.pay.model.PayCode
import java.time.LocalDateTime
import java.util.*

@NamedEntityGraph(
	name = "payDataWithProduct",
	attributeNodes = [NamedAttributeNode("productEntity")]
)

@Entity
@Table(name = "pay_data", schema = "shop", catalog = "medalist")
class PayDataEntity(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	var id: Long? = null,

	@Column(name = "date_op")
	var dateOp: LocalDateTime = LocalDateTime.now(),

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	var userEntity: UserEntity? = null,

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id")
	var productEntity: ProductEntity? = null,

	var price: Int = 0,

	@Column(name = "pay_code")
	var payCode: PayCode = PayCode.UNDEF,

	@Column(name = "is_active")
	var isActive: Boolean = false,

	) {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || javaClass != other.javaClass) return false
		val that = other as PayDataEntity
		return id == that.id && price == that.price && dateOp == that.dateOp
	}

	override fun hashCode(): Int {
		return Objects.hash(id, dateOp, price)
	}
}
