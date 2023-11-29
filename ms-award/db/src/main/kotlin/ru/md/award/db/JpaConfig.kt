package ru.md.award.db

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EntityScan("ru.md.base_db", "ru.md.award.db")
@EnableJpaRepositories("ru.md.base_db", "ru.md.award.db")
class JpaConfig