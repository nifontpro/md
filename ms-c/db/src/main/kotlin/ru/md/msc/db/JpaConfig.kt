package ru.md.msc.db

import org.springframework.boot.autoconfigure.AutoConfigurationPackage
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration

// https://stackoverflow.com/questions/71064231/spring-import-jpa-repositories-from-another-module

@Configuration
@AutoConfigurationPackage
@EntityScan("ru.md.base_db", "ru.md.msc")
class JpaConfig