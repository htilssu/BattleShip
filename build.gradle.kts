plugins {
    id("java")
    kotlin("jvm")
}

group = "com.htilssu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
tasks.compileJava {
    options.encoding = "UTF-8"
}
kotlin {
    jvmToolchain(21)
}