@file:Suppress("UnstableApiUsage")

// 相关接口自API 31开始提供，因此本库只能用在最低API大于或等于31的项目中。
val versionMinSDK = 31
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
        applicationId = "net.bi4vmr.tool.android.ui.crosswindowblurtool"
        minSdk = versionMinSDK
        targetSdk = versionTargetSDK
        versionCode = versionCode
        versionName = versionName
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

    implementation(project(":lib_ui:crosswindowblurtool"))
    implementation(project(":lib_ability:framework"))
}
