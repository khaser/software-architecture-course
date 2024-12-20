val zirconVersion: String by project

plugins {
    kotlin("jvm") version "2.0.20"
    kotlin("plugin.serialization") version "2.0.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1"
}

group = "ru.spbu.krogue"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    implementation("org.hexworks.zircon:zircon.core-jvm:$zirconVersion")
    implementation("org.hexworks.zircon:zircon.jvm.swing:$zirconVersion")
}

ktlint {
    verbose.set(true)
    outputToConsole.set(true)
    coloredOutput.set(true)
    filter {
        exclude("**/style-violations.kt")
        exclude("**/generated/**")
    }
}
