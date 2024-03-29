import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot")
	id("io.spring.dependency-management")
	kotlin("jvm")
	kotlin("plugin.spring")
}

java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val springCloudVersion: String by project
val kotlinLoggingVersion: String by project
val jvmTargetVersion: String by project
val sshAntTask = configurations.create("sshAntTask")

dependencies {
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")
	sshAntTask("org.apache.ant:ant-jsch:1.10.12")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = jvmTargetVersion
	}
}

val jarFileName = "proxy.jar"
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

task("remote-proxy") {
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
					"todir" to "$user@$host:~/v1/md/proxy",
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
					"command" to "cd ~/v1/md/proxy; docker compose build; docker compose up -d"
				)
			} finally {
				knownHosts.delete()
			}
		}
	}
}