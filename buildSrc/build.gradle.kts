plugins {
	`kotlin-dsl`
}

repositories {
	mavenCentral()
	google()
}

dependencies {
	implementation("com.android.tools.build:gradle:7.1.3")
	implementation(kotlin("gradle-plugin"))
}