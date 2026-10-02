@file:Suppress("UnstableApiUsage")

// 相关接口自API 30开始提供，因此本库只能用在最低API大于或等于30的项目中。
val versionMinSDK = 30
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
        applicationId = "net.bi4vmr.tool.android.ability.privacymonitor"
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

    compileOnly(project(":lib_ability:framework"))
    runtimeOnly(privateLibAndroid.ability.framework)
    compileOnly(project(":lib_ability:privacymonitor"))
    runtimeOnly(privateLibAndroid.ability.privacyMonitor)
}
