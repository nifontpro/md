plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
}

dependencies {
	implementation(project(":cor"))
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}