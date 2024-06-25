plugins {
    id("java")
}

group = "com.htilssu"
version = ""

repositories {
    mavenCentral()
}
val gsonVersion = "2.8.9"

dependencies {
    implementation("org.jetbrains:annotations:24.0.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.google.code.gson:gson:$gsonVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.test {
    useJUnitPlatform()
}
tasks.compileJava {
    options.encoding = "UTF-8"
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.htilssu.BattleShip"
    }
    from(sourceSets.main.get().output)
}