import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Exe
import org.jetbrains.compose.desktop.application.dsl.TargetFormat.Pkg
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.UUID

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.buildconfig)
    kotlin("plugin.serialization") version "2.0.20"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Gluky"
            isStatic = true
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "composeApp"
        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "Gluky.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                }
            }
        }
        binaries.executable()
    }

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.app.update)
            implementation(libs.app.update.ktx)
            implementation(libs.review)
            implementation(libs.review.ktx)
            implementation(libs.androidx.fragment.ktx)
        }

        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.equinox.compose)
                implementation(libs.equinox.core)
                implementation(libs.equinox.navigation)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.jetlime)
                implementation(libs.richeditor.compose)
                implementation(libs.sonner)
                implementation(libs.compose.charts)
                implementation(libs.filekit.core)
                implementation(libs.filekit.dialogs.compose)
                implementation(libs.navigation.compose)
                implementation(libs.glukycore)
            }
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.octocatkdu)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }

    }
    jvmToolchain(18)
}

android {
    namespace = "com.tecknobit.gluky"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.tecknobit.gluky"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 3
        versionName = "1.0.2"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.tecknobit.gluky.MainKt"
        nativeDistributions {
            targetFormats(Deb, Pkg, Exe)
            modules(
                "java.compiler",
                "java.instrument",
                "java.management",
                "java.net.http",
                "java.prefs",
                "java.rmi",
                "java.scripting",
                "java.security.jgss",
                "java.sql.rowset",
                "jdk.jfr",
                "jdk.unsupported",
                "jdk.security.auth"
            )
            packageName = "Gluky"
            packageVersion = "1.0.2"
            version = "1.0.2"
            description = "A daily tracker for glucose levels and meals"
            copyright = "© 2025 Tecknobit"
            vendor = "Tecknobit"
            licenseFile.set(project.file("src/desktopMain/resources/LICENSE"))
            macOS {
                bundleID = "com.tecknobit.gluky"
                iconFile.set(project.file("src/desktopMain/resources/logo.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/logo.ico"))
                upgradeUuid = UUID.randomUUID().toString()
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/logo.png"))
                packageName = "com-tecknobit-gluky"
                debMaintainer = "infotecknobitcompany@gmail.com"
                appRelease = "1.0.2"
                appCategory = "PERSONALIZATION"
                rpmLicenseType = "APACHE2"
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("src/desktopMain/resources/compose-desktop.pro"))
            obfuscate.set(true)
        }
    }
}

dokka {
    moduleName.set("Gluky")
    dokkaPublications.html {
        outputDirectory.set(layout.projectDirectory.dir("../docs"))
    }
    pluginsConfiguration {
        versioning {
            version.set("1.0.2")
        }
        html {
            customAssets.from("../images/logo-icon.svg")
            footerMessage.set("(c) 2025 Tecknobit")
        }
    }
}

buildConfig {
    className("GlukyConfig")
    packageName("com.tecknobit.gluky")
    buildConfigField<String>(
        name = "LOCAL_STORAGE_PATH",
        value = project.findProperty("localStoragePath").toString()
    )
}