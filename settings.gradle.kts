rootProject.name = "md"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val springBootVersion: String by settings
        val springDependencyVersion: String by settings
        val shadowVersion: String by settings

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        kotlin("plugin.allopen") version kotlinVersion apply false
		id("com.github.johnrengelman.shadow") version shadowVersion apply false
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyVersion apply false

    }
}


include("config")


