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
	maven { url = uri("https://artifactory-oss.prod.netflix.net/artifactory/maven-oss-candidates") }
}

extra["springCloudVersion"] = "2022.0.2"

val sshAntTask = configurations.create("sshAntTask")

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	sshAntTask("org.apache.ant:ant-jsch:1.10.12")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
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
