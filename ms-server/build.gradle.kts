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
//	maven { url = uri("https://repo.spring.io/milestone") }
}

val springCloudVersion: String by project
extra["springCloudVersion"] = springCloudVersion

val sshAntTask = configurations.create("sshAntTask")

dependencies {
	implementation(kotlin("stdlib-jdk8"))
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

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
		jvmTarget = "17"
	}
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
	this.archiveFileName.set("server.jar")
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

task("remote-server") {
	dependsOn("bootJar")
	ant.withGroovyBuilder {
		doLast {
			val knownHosts = File.createTempFile("knownhosts", "txt")
			val user = "nifont"
			val host = remoteUrl
			val key = file(patchKey)
			val jarFileName = "server.jar"
			try {
				"scp"(
					"file" to file("build/libs/$jarFileName"),
					"todir" to "$user@$host:~/v1/md/server",
					"keyfile" to key,
					"trust" to true,
					"knownhosts" to knownHosts
				)
//                "ssh"(
//                    "host" to host,
//                    "username" to user,
//                    "keyfile" to key,
//                    "trust" to true,
//                    "knownhosts" to knownHosts,
//                    "command" to "cd ~/v1/md; docker compose build; docker compose up -d"
//                )
			} finally {
				knownHosts.delete()
			}
		}
	}
}
//kotlin {
//    jvmToolchain(11)
//}