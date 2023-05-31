package ru.md.msc.db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.msc.db.user.model.UserSettingsEntity

@Repository
interface UserSettingsRepository : JpaRepository<UserSettingsEntity, Long>