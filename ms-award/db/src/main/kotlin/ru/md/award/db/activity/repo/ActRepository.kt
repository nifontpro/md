package ru.md.award.db.activity.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.award.db.activity.model.ActEntity

@Repository
interface ActRepository : JpaRepository<ActEntity, Long>