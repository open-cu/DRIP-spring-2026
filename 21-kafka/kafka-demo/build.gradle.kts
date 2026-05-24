plugins {
    id("io.spring.dependency-management") version "1.1.7"
}

allprojects {
    group = "demo"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<org.gradle.api.plugins.JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    dependencies {
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"(platform("org.testcontainers:testcontainers-bom:1.20.4"))
        "testImplementation"("org.testcontainers:junit-jupiter")
        "testImplementation"("org.testcontainers:kafka")
        "testImplementation"("org.awaitility:awaitility:4.2.1")
    }

    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.5.9")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
