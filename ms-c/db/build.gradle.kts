import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
	maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
}

val springCloudVersion: String by project
val jvmTargetVersion: String by project
val sshAntTask = configurations.create("sshAntTask")
extra["springCloudVersion"] = springCloudVersion

dependencies {
	implementation(project(":ms-c:domain"))
	implementation(project(":ms-c:rest"))
	implementation(project(":ms-c:s3"))

	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")

	sshAntTask("org.apache.ant:ant-jsch:1.10.12")
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

val jarFileName = "client.jar"
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	this.archiveFileName.set(jarFileName)
}

ant.withGroovyBuilder {
	"taskdef"(
		"name" to "scp",
		"classname" to "org.apache.tools.ant.taskdefs.optional.ssh.Scp",
		"classpath" to configurations["sshAntTask"].asPath
	)
	"taskdef"(
		"name" to "ssh",
		"classname" to "org.apache.tools.ant.taskdefs.optional.ssh.SSHExec",
		"classpath" to configurations["sshAntTask"].asPath
	)
}

val remoteUrl = "nmedalist.ru"
val myFolder = System.getenv("MY_FOLDER") ?: "~"
val patchKey = "$myFolder/Deploy/serverkey"

task("remote-client") {
	dependsOn("bootJar")
	ant.withGroovyBuilder {
		doLast {
			val knownHosts = File.createTempFile("knownhosts", "txt")
			val user = "nifont"
			val host = remoteUrl
			val key = file(patchKey)
			try {
				"scp"(
					"file" to file("build/libs/$jarFileName"),
					"todir" to "$user@$host:~/v1/md/client",
					"keyfile" to key,
					"trust" to true,
					"knownhosts" to knownHosts
				)
				"ssh"(
					"host" to host,
					"username" to user,
					"keyfile" to key,
					"trust" to true,
					"knownhosts" to knownHosts,
					"command" to "cd ~/v1/md; docker compose build; docker compose up -d"
				)
			} finally {
				knownHosts.delete()
			}
		}
	}
}
