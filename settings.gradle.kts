rootProject.name = "md"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        val springBootVersion: String by settings
        val springDependencyVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.jpa") version kotlinVersion apply false
        kotlin("plugin.allopen") version kotlinVersion apply false
        id("org.springframework.boot") version springBootVersion apply false
        id("io.spring.dependency-management") version springDependencyVersion apply false

    }
}

include("cor")
include("ms-server")
include("ms-gateway")
include("front")
//include("proxy")

include("ms-c")
include("ms-c:db")
include("ms-c:domain")
include("ms-c:rest")
include("ms-c:s3")

include("base")
include("base:rest")
include("base:dom")

include("ms-gal")
include("ms-gal:db")
include("ms-gal:domain")
include("ms-gal:rest")
