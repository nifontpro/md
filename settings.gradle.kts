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

include("base")
include("base:base_db")
include("base:base_domain")
include("base:base_rest")
include("base:base_client")
include("base:base_s3")

include("ms-gal")
include("ms-gal:db")
include("ms-gal:domain")
include("ms-gal:rest")

include("ms-shop")
include("ms-shop:db")
include("ms-shop:domain")
include("ms-shop:rest")

include("ms-award")
include("ms-award:db")
include("ms-award:domain")
include("ms-award:rest")
