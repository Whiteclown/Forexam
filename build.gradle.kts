// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
	id(Dependencies.Plugins.ANDROID_APPLICATION) apply false
	id(Dependencies.Plugins.ANDROID_LIBRARY) apply false
	id(Dependencies.Plugins.KOTLIN_ANDROID) apply false
}

buildscript {
	repositories {
		google()
		mavenCentral()
	}

	dependencies {
		classpath(Dependencies.Plugins.HILT_GRADLE)
		classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.4.1")
		classpath("org.jetbrains.kotlin:kotlin-serialization:1.6.10")
	}
}

tasks.register("clean", Delete::class) {
	delete(rootProject.buildDir)
}