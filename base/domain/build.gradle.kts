plugins {
    kotlin("jvm")
}

val coroutinesVersion: String by project

dependencies {
    implementation(project(":cor"))
    
    implementation(kotlin("stdlib-common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
}