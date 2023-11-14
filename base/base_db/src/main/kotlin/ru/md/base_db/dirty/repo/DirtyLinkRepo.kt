package ru.md.base_db.dirty.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.base_db.dirty.model.DirtyLinkEntity

@Repository
interface DirtyLinkRepo : JpaRepository<DirtyLinkEntity, Long>