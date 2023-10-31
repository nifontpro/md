package ru.md.shop.db.pay.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.shop.db.pay.model.TrashEntity

@Repository
interface TrashRepo : JpaRepository<TrashEntity, Long> {

}