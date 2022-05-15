object Dependencies {

	object Plugins {

		const val ANDROID_GRADLE = "com.android.tools.build:gradle:7.1.3"
		const val KOTLIN_GRADLE = "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10"
		const val HILT_GRADLE = "com.google.dagger:hilt-android-gradle-plugin:2.38.1"
		const val HILT = "dagger.hilt.android.plugin"
		const val ANDROID_APPLICATION = "com.android.application"
		const val ANDROID_LIBRARY = "com.android.library"
		const val KOTLIN_ANDROID = "org.jetbrains.kotlin.android"
		const val KAPT = "kapt"
		const val SERIALIZATION = "plugin.serialization"
	}

	object Versions {

		const val SERIALIZATION = "1.6.10"
	}

	object Hilt {

		const val COMPILER = "com.google.dagger:hilt-compiler:2.38.1"
		const val ANDROID = "com.google.dagger:hilt-android:2.38.1"
	}

	object Core {

		const val CORE = "androidx.core:core-ktx:1.7.0"
		const val APPCOMPAT = "androidx.appcompat:appcompat:1.4.1"
		const val MATERIAL = "com.google.android.material:material:1.5.0"
	}

	object Network {

		const val RETROFIT = "com.squareup.retrofit2:retrofit:2.9.0"
		const val OKHTTP = "com.squareup.okhttp3:okhttp:4.9.0"
		const val OKHTTP_LOGGING_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.9.0"
		const val KOTLINX_SERIALIZATION = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"
		const val KOTLINX_CONVERTER = "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
	}

	object Navigation {

		const val FRAGMENT = "androidx.navigation:navigation-fragment-ktx:2.4.2"
		const val UI = "androidx.navigation:navigation-ui-ktx:2.4.2"
		const val DYNAMIC_FEATURES = "androidx.navigation:navigation-dynamic-features-fragment:2.4.2"
	}

	object Layout {

		const val RECYCLER_VIEW = "androidx.recyclerview:recyclerview:1.2.1"
		const val CONSTRAINT = "androidx.constraintlayout:constraintlayout:2.1.3"
	}
}