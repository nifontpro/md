import org.gradle.kotlin.dsl.java
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

project.version = "1.0.0"

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
	kotlin("plugin.jpa")
	kotlin("plugin.allopen")
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.Embeddable")
	annotation("jakarta.persistence.MappedSuperclass")
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
//	maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
//	maven { url = uri("https://repo.spring.io/milestone") }
}

val springCloudVersion: String by project
val jvmTargetVersion: String by project
extra["springCloudVersion"] = springCloudVersion

dependencies {
	implementation(project(":base:base_db"))
	implementation(project(":base:base_domain"))
	implementation(project(":base:base_client"))
	implementation(project(":base:base_s3"))

	implementation(project(":ms-shop:domain"))
	implementation(project(":ms-shop:rest"))

	implementation("net.logstash.logback:logstash-logback-encoder:7.3")

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// Load Balancer cache
	implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = jvmTargetVersion
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	this.archiveFileName.set("shop.jar")
}

tasks.withType(BootBuildImage::class) {
	this.imageName.set("8881981/md_shop:1.0.0")
}
