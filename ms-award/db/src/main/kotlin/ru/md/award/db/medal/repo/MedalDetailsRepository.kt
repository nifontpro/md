package ru.md.award.db.medal.repo

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.award.db.medal.model.MedalDetailsEntity

@Repository
interface MedalDetailsRepository : JpaRepository<MedalDetailsEntity, Long> {

	@EntityGraph("medalDetailsWithDept")
	fun findByMedalId(medalId: Long): MedalDetailsEntity?

}