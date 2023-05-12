package ru.md.msgal.db.item.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msgal.db.item.model.ItemEntity

@Repository
interface ItemRepository : JpaRepository<ItemEntity, Long>