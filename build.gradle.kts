import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

val kotlinVersion: String by project
val micronautVersion: String by project

plugins {
    id("io.micronaut.application") version "1.1.0"
    kotlin("jvm") version "1.4.10"
    kotlin("kapt") version "1.4.10"

    id("io.spring.dependency-management") version "1.0.6.RELEASE"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.4.10"
    id ("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "org.hshekhar"
version = "1.0.0"


application {
    mainClass.set("org.hshekhar.Application")
}

micronaut {
    runtime("netty")
    processing {
        incremental(true)
        annotations("${project.group}.*")
    }
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))

    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-http-server-netty")
    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    kapt("io.micronaut:micronaut-inject-java:$micronautVersion")

    // Swagger config
    kapt("io.micronaut.configuration:micronaut-openapi")
    implementation("io.swagger.core.v3:swagger-annotations")

    // Security, jwt
    kapt("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut.security:micronaut-security-jwt")

}

the<DependencyManagementExtension>().apply {
    imports {
        mavenBom("io.micronaut:micronaut-bom:$micronautVersion")
    }
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
}