plugins {
	kotlin("jvm")
}

dependencies {
	implementation(project(":cor"))
	implementation(kotlin("stdlib-common"))

	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

	implementation(kotlin("test-common"))
	implementation(kotlin("test-annotations-common"))
	testImplementation(kotlin("test-junit"))
}