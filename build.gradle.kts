plugins {
	java
	id("org.springframework.boot") version "3.5.11"
	id("io.spring.dependency-management") version "1.1.7"
	checkstyle
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.liquibase:liquibase-core")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootJar {
	archiveFileName.set("service.jar")
}

checkstyle {
	toolVersion = "13.2.0"
	configFile = file("${project.rootDir}/config/checkstyle/checkstyle.xml")
	checkstyle.enableExternalDtdLoad.set(true)
}

tasks.checkstyleMain {
	source = fileTree("${project.rootDir}/src/main/java")
	include("**/*.java")
	exclude("**/resources/**")

	classpath = files()
}

tasks.checkstyleTest {
	source = fileTree("${project.rootDir}/src/test")
	include("**/*.java")

	classpath = files()
}
