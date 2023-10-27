package ru.md.shop.db

import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@AutoConfigurationPackage
@EntityScan("ru.md.base_db", "ru.md.shop")
@EnableJpaRepositories("ru.md.base_db", "ru.md.shop")
class JpaConfig