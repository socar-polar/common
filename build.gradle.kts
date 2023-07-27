val currentVersion: String by project

plugins {
	alias(libs.plugins.kotlin.jvm)
	alias(libs.plugins.ktlint)
	id("idea")
	id("project-report")
	alias(libs.plugins.kotlin.kapt)
}

repositories {
	mavenCentral()
}

allprojects {
	group = "com.polar"
	version = currentVersion
}

dependencies {
	implementation(kotlin("stdlib"))
}

subprojects {
	val libs = rootProject.libs
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "java")
	apply(plugin = "idea")
	apply(plugin = "project-report")
	apply(plugin = "maven-publish")
	apply(plugin = "org.jetbrains.kotlin.kapt")

	repositories {
		mavenCentral()
		gradlePluginPortal()
	}

	dependencies {
		implementation(platform(libs.spring.boot.dependencies))
		api(libs.bundles.kotlin.stdlib)
	}

	java {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
		withSourcesJar()
	}

	tasks.compileKotlin {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.compileTestKotlin {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	configure<PublishingExtension> {
		repositories {
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/socar-polar/common")
				credentials {
					username = System.getenv("COMMON_GITHUB_USERNAME")
					password = System.getenv("COMMON_GITHUB_TOKEN")
				}
			}
		}
		publications {
			create<MavenPublication>("mavenJava") {
				from(components["java"])
			}
		}
	}
}
