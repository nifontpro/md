package ru.md.msc.db.msg.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.msg.model.UserMsgEntity


@Repository
interface MessageRepository : JpaRepository<UserMsgEntity, Long> {

	fun findByToId(toId: Long): List<UserMsgEntity>

}