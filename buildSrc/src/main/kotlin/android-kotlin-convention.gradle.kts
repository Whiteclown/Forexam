import com.android.build.gradle.BaseExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

android {
	defaultConfig {
		proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		compileSdkVersion(31)
		minSdk = 21
		targetSdk = 31
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		getByName(BuildTypes.RELEASE) {
			isMinifyEnabled = true
			isDebuggable = false
		}

		getByName(BuildTypes.DEBUG) {
			isMinifyEnabled = false
			isDebuggable = true
		}
	}

	viewBinding {
		isEnabled = true
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}

	(this as ExtensionAware).configure<KotlinJvmOptions> {
		jvmTarget = JavaVersion.VERSION_1_8.toString()
	}
}

fun Project.android(configure: BaseExtension.() -> Unit) {
	extensions.configure("android", configure)
}