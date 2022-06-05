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

	implementation(Dependencies.Navigation.FRAGMENT)

	implementation(Dependencies.Layout.RECYCLER_VIEW)
	implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

	implementation(project(":shared:exam"))
	implementation(project(":shared:session"))
	implementation(project(":core"))
}