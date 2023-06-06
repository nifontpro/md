package ru.md.msc.db.event.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.event.model.UserEventEntity

@Repository
interface UserEventRepository : JpaRepository<UserEventEntity, Long>