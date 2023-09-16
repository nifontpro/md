package ru.md.msc.db.medal.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.medal.model.MedalDetailsEntity

@Repository
interface MedalDetailsRepository : JpaRepository<MedalDetailsEntity, Long>