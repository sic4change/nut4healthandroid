buildscript {

    dependencies {
        classpath ("com.google.gms:google-services:4.3.15")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:${project.property("hilt_version")}")
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}