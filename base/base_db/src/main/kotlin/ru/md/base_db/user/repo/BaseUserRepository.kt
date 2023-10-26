package ru.md.base_db.user.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.md.base_db.user.model.UserEntity

@Repository
interface BaseUserRepository : JpaRepository<UserEntity, Long>