@file:Suppress("UnstableApiUsage")

val versionMinSDK: Int = agp.versions.minSdk.get().toInt()
val versionCompileSDK: Int = agp.versions.compileSdk.get().toInt()
val versionTargetSDK: Int = agp.versions.targetSdk.get().toInt()
val versionCode: Int = agp.versions.versionCode.get().toInt()
val versionName: String = agp.versions.versionName.get()

plugins {
    alias(libAndroid.plugins.application)

    alias(privateLibJava.plugins.java.version)
}

android {
    namespace = "net.bi4vmr.tool"
    compileSdk = versionCompileSDK

    defaultConfig {
        applicationId = "net.bi4vmr.tool.android.ui.universal"
        minSdk = versionMinSDK
        targetSdk = versionTargetSDK
        this.versionCode = versionCode
        this.versionName = versionName
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

    viewBinding {
        enable = true
    }
}

dependencies {
    implementation(libAndroid.bundles.appBaseKT)

    // 本地依赖
    implementation(project(":lib_ui:universal"))
    // 远程依赖
    // implementation(privateLibAndroid.ui.universal)
}
