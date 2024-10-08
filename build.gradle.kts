plugins {
    java
    id("org.springframework.boot") version "3.2.2"
    id("io.spring.dependency-management") version "1.1.4"
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath("com.google.cloud.tools:appengine-gradle-plugin:2.8.0")
    }
}

apply(plugin = "com.google.cloud.tools.appengine")

configure<com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension> {
    deploy {
        projectId = "GCLOUD_CONFIG"
        version = "GCLOUD_CONFIG"
    }
}

group = "com.noisevisionproduction"
version = "5.1"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

configurations.all {
    exclude(group = "com.vaadin.external.google", module = "android-json")
}

dependencies {
    // Basics of spring boot dependencies
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation("org.json:json:20240303")
    // Firebase dependencies
    implementation("com.google.firebase:firebase-admin:9.2.0")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // Tests dependencies
    testImplementation("org.mockito:mockito-inline:5.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}