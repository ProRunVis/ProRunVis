plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")


    //spring boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.7.8"))
}

tasks.test {
    useJUnitPlatform()
}