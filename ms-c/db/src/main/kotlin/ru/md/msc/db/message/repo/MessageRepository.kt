package ru.md.msc.db.message.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.message.model.UserMsgEntity


@Repository
interface MessageRepository : JpaRepository<UserMsgEntity, Long> {

	fun findByToId(toId: Long): List<UserMsgEntity>

}