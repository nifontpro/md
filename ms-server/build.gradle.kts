import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm")
	kotlin("plugin.spring")
	id("org.springframework.boot")
	id("io.spring.dependency-management")
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val springCloudVersion: String by project
extra["springCloudVersion"] = springCloudVersion

val sshAntTask = configurations.create("sshAntTask")

dependencies {
//	implementation("net.logstash.logback:logstash-logback-encoder:7.3")

	implementation(kotlin("stdlib-jdk8"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	this.archiveFileName.set("server.jar")
}

//kotlin {
//    jvmToolchain(11)
//}