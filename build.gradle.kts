import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    
    implementation("club.minnced:discord-webhooks:0.8.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2")
    
    api("org.slf4j:slf4j-api:2.0.7")
    api("ch.qos.logback:logback-classic:1.4.7")
    api("ch.qos.logback:logback-core:1.4.8")
    
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}