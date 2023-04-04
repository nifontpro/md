import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") apply false
    id("io.spring.dependency-management")  apply false
}

allprojects {

    group = "ru.md"
    version = "1.0-SNAPSHOT"


    repositories {
        mavenCentral()
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
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    apply {
        plugin("io.spring.dependency-management")
    }
}

//kotlin {
//    jvmToolchain(11)
//}

//application {
//    mainClass.set("MainKt")
//}