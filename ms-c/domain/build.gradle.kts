plugins {
	kotlin("jvm")
}

val coroutinesVersion: String by project
val kotlinLoggingVersion: String by project

dependencies {
	implementation(project(":cor"))
	implementation(kotlin("stdlib-common"))
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
	implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")

	implementation(kotlin("test-common"))
	implementation(kotlin("test-annotations-common"))
	testImplementation(kotlin("test-junit"))
}