plugins {
	id(Dependencies.Plugins.ANDROID_APPLICATION)
	id(Dependencies.Plugins.KOTLIN_ANDROID)
	kotlin(Dependencies.Plugins.KAPT)
	id(Dependencies.Plugins.HILT)
	id("androidx.navigation.safeargs.kotlin")
	`android-kotlin-convention`
}

android {
	defaultConfig {
		applicationId = "com.bobrovskii.forexam"

		versionCode = 1
		versionName = "1.0"
	}
}

dependencies {
	kapt(Dependencies.Hilt.COMPILER)
	implementation(Dependencies.Hilt.ANDROID)

	implementation(Dependencies.Core.CORE)
	implementation(Dependencies.Core.APPCOMPAT)
	implementation(Dependencies.Core.MATERIAL)

	implementation(Dependencies.Navigation.UI)
	implementation(Dependencies.Navigation.FRAGMENT)
	implementation(Dependencies.Navigation.DYNAMIC_FEATURES)

	implementation(Dependencies.Layout.CONSTRAINT)

	implementation(Dependencies.Network.RETROFIT)
	implementation("com.squareup.moshi:moshi:1.13.0")
	implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
	implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
	implementation(Dependencies.Network.OKHTTP)
	implementation(Dependencies.Network.OKHTTP_LOGGING_INTERCEPTOR)

	implementation(project(":features:signin"))
	implementation(project(":features:signup"))
	implementation(project(":features:home"))
	implementation(project(":features:editexamination"))
	implementation(project(":features:addexamination"))
	implementation(project(":features:accessexamination"))
	implementation(project(":shared:session"))
	implementation(project(":shared:exam"))
}