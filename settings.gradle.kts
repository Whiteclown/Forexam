pluginManagement {
	repositories {
		gradlePluginPortal()
		google()
		mavenCentral()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
	}
}
rootProject.name = "Forexam"
include(
	":app",
	":core",
	":features:signin",
	":features:signup",
	":features:home",
	":features:editexamination",
	":features:addexamination",
	":features:answerslist",
	":features:studentslist",
	":features:answer",
	":shared:session",
	":shared:exam",
	":shared:artefact",
)
