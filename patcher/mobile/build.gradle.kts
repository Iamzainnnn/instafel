plugins {
    alias(libs.plugins.android.application)
}

/************************************************/
/* BUILD CONFIG INITIALIZATION PASHE */

var config = rootProject.extra["instafelConfig"] as Map<*, *>
val projectConfig = config["patcher"] as Map<*, *>
val androidConfig = projectConfig["mobileAndroidConfig"] as Map<*, *>
val keystoreConfig = androidConfig["keystore"] as Map<*, *>
val depsConfig = projectConfig["dependencyConfig"] as Map<*, *>
val pLibs = rootProject.extra["patcherLibs"] as Map<*, *>

val projectVersion = projectConfig["mobile_version"] as String

val commitHash: String by rootProject.extra

group = "me.mamiiblt.instafel"

/************************************************/

repositories {
    google {
        content {
            includeGroupByRegex("com\\.android.*")
            includeGroupByRegex("com\\.google.*")
            includeGroupByRegex("androidx.*")
        }
    }
    mavenCentral()
    gradlePluginPortal()
    maven("https://jitpack.io")
}

android {
    namespace = "me.mamiiblt.instafel.patcher.mobile"
    compileSdk = 34

    // disable include metadata in dep infos
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    defaultConfig {
        applicationId = "me.mamiiblt.instafel.patcher.mobile"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = projectVersion
        buildConfigField("String", "COMMIT", "\"$commitHash\"")
        buildConfigField("String", "BRANCH", "\"main\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs{
        create("release") {
            storeFile = File(rootDir, keystoreConfig["ksPath"] as String)
            storePassword = keystoreConfig["ksKeyPass"] as String
            keyAlias = keystoreConfig["ksAlias"] as String
            keyPassword = keystoreConfig["ksPass"] as String
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.register("generate-app-debug") {
    dependsOn("clear-cache", "assembleDebug")

    doLast {
        val apkDir = file("${buildDir}/outputs/apk/debug")
        val apkFile = apkDir.listFiles()?.firstOrNull { it.extension == "apk" }

        requireNotNull(apkFile) { "❌ No APK file found in debug folder!" }

        val outputName = "ifl-pmobile-v$projectVersion-$commitHash-debug.apk"
        apkFile.copyTo(file("${project.projectDir}/output/$outputName"), overwrite = true)

        println("✅ APK successfully copied: $outputName")

        delete("${project.projectDir}/build")
        println("🧹 Build caches cleared.")
        println("✅ All tasks completed successfully")
    }
}

tasks.register("generate-app-release") {
    dependsOn("clear-cache", "assembleRelease")

    doLast {
        val outputName = "ifl-updater-v$projectVersion-$commitHash-release.apk"
        file("${project.projectDir}/build/outputs/apk/release/instafel.updater-release.apk")
            .copyTo(file("${project.projectDir}/output/$outputName"), overwrite = true)
        println("APK successfully copied: $outputName")

        delete("${project.projectDir}/build")
        println("Build caches cleared.")
        println("All tasks completed succesfully")
    }
}

tasks.register("clear-cache") {
    val filesToDelete = listOf(
        file("${project.projectDir}/build"),
        file("${project.projectDir}/output"),
    )

    delete(filesToDelete)
    doLast {
        println("Cache successfully deleted.")
    }
}

dependencies {
    implementation(pLibs["org-json"]!!)
    implementation(pLibs["commons-io"]!!)
    implementation(pLibs["okhttp"]!!)
    implementation(pLibs["apktool-lib"]!!)
    implementation(pLibs["classgraph"]!!)
    implementation(pLibs["jackson-databind"]!!)
    implementation(pLibs["jackson-yaml"]!!)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation("com.squareup.okhttp3:okhttp:${depsConfig["okhttp_version"] as String}")
    implementation("com.github.TTTT55:Material-You-Preferences:${depsConfig["materialyoupreferences_version"] as String}")
    implementation(libs.navigation.ui)
    implementation(libs.preference)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
