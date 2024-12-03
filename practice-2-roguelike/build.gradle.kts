val kotlin_version: String by project

plugins {
    kotlin("jvm") version "2.0.20"
}

group = "ru.spbu.krogue"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.michael-bull.kotlin-result:kotlin-result:2.0.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:2.0.20")
}
