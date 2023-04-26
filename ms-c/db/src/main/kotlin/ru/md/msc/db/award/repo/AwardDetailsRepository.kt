package ru.md.msc.db.award.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.award.model.AwardDetailsEntity

@Repository
interface AwardDetailsRepository : JpaRepository<AwardDetailsEntity, Long>