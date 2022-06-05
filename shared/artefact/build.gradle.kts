plugins {
	id(Dependencies.Plugins.ANDROID_LIBRARY)
	id(Dependencies.Plugins.KOTLIN_ANDROID)
	kotlin(Dependencies.Plugins.KAPT)
	id(Dependencies.Plugins.HILT)
	`android-kotlin-convention`
}

dependencies {
	kapt(Dependencies.Hilt.COMPILER)
	implementation(Dependencies.Hilt.ANDROID)

	implementation(Dependencies.Core.CORE)
	implementation(Dependencies.Core.APPCOMPAT)
	implementation(Dependencies.Core.MATERIAL)

	implementation(Dependencies.Network.RETROFIT)
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
	implementation("com.squareup.okhttp3:okhttp:4.9.0")
	implementation("com.squareup.moshi:moshi:1.13.0")
	implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
	kapt("com.squareup.moshi:moshi-kotlin-codegen:1.13.0")

	implementation(project(":core"))
}