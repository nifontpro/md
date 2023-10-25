package ru.md.shop.db

import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration

@Configuration
@AutoConfigurationPackage
@EntityScan("ru.md.base_db", "ru.md.shop")
class JpaConfig