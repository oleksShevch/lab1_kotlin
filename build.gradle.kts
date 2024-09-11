plugins {
    kotlin("jvm") version "2.0.10"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.0") // Replace with your version
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.7.0") // Add this line


}

javafx {
    version = "22.0.1"
    modules("javafx.controls")
}

kotlin {
    jvmToolchain(17)
}