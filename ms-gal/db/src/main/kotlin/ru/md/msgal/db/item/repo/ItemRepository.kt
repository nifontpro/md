package ru.md.msgal.db.item.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msgal.db.item.model.ItemEntity

@Repository
interface ItemRepository : JpaRepository<ItemEntity, Long> {

	fun findByFolderId(folderId: Long, pageable: Pageable): Page<ItemEntity>

}