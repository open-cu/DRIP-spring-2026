plugins {
    id("org.springframework.boot") version "3.5.9"
}

dependencies {
    implementation(project(":common-domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.kafka:kafka-streams")

    testImplementation(project(":test-support"))
    testImplementation("org.springframework.kafka:spring-kafka-test")
}
