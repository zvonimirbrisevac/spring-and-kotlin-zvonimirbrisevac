import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.5.20"
	kotlin("plugin.spring") version "1.5.20"
	kotlin("plugin.jpa") version "1.5.10"

}

group = "com.infinum"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

extra["testcontainersVersion"] = "1.15.3"


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.flywaydb:flyway-core")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("net.javacrumbs.shedlock:shedlock-provider-jdbc-template:4.24.0")
	implementation("net.javacrumbs.shedlock:shedlock-spring:4.24.0")
	implementation("org.springframework.boot:spring-boot-starter-hateoas")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.ninja-squad:springmockk:3.0.1")
	testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
	testImplementation("org.assertj:assertj-core:3.20.2")
	testImplementation("io.mockk:mockk:1.12.0")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.mock-server:mockserver-spring-test-listener:5.11.2")

}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
