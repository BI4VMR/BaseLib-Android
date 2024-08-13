val versionMinSDK = 30
val versionCompileSDK: Int = agp.versions.compileSdk.get().toInt()
val versionTargetSDK: Int = agp.versions.targetSdk.get().toInt()
val versionModuleCode: Int = agp.versions.moduleCode.get().toInt()
val versionModuleName: String = agp.versions.moduleName.get()

plugins {
    alias(libAndroid.plugins.application)
    alias(libAndroid.plugins.kotlinSupport)
}

@Suppress("UnstableApiUsage")
android {
    namespace = "net.bi4vmr.tool"
    compileSdk = versionCompileSDK

    defaultConfig {
        applicationId = "net.bi4vmr.tool.android.ability.privacymonitor"
        minSdk = versionMinSDK
        targetSdk = versionTargetSDK
        versionCode = versionModuleCode
        versionName = versionModuleName
    }

    signingConfigs {
        create("AOSP") {
            storeFile =
                file("${rootDir.absolutePath}${File.separator}misc/keystore/AOSP.keystore")
            storePassword = "AOSPSystem"
            keyAlias = "AOSPSystem"
            keyPassword = "AOSPSystem"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("AOSP")
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("AOSP")
        }
    }

    sourceSets {
        getByName("main") {
            java {
                java.srcDir("src/main/kotlin")
            }
        }
    }

    compileOptions {
        // 指定Java源码编译目标版本
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        // 指定Kotlin源码编译目标版本
        jvmTarget = "11"
    }

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libAndroid.bundles.baseWithKT)

    // 本地依赖
    implementation(project(":lib_ability:privacymonitor"))
    // 远程依赖
    // implementation(privateLibAndroid.ability.privacyMonitor)
}
