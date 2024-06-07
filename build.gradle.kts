plugins {
    id("java")
    kotlin("jvm")
}

group = "com.htilssu"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val gsonVersion = "2.8.9"

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.code.gson:gson:$gsonVersion")
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