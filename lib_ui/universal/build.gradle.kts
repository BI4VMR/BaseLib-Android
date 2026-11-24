@file:Suppress("UnstableApiUsage")

val versionMinSDK: Int = Integer.valueOf(agp.versions.minSdk.get())
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.ui.universal.get()
val mvnGroupID: String = requireNotNull(depInTOML.group)
val mvnArtifactID: String = depInTOML.name
val mvnVersion: String = requireNotNull(depInTOML.version)

plugins {
    alias(libAndroid.plugins.library)

    alias(privateLibJava.plugins.java.version)
    alias(privateLibJava.plugins.publish.private)
}

// 当前使用 AGP 9，已内置 KGP 插件，无法通过插件版本指明 Kotlin 版本，需要通过以下配置明确声明。
kotlin {
    val kotlinVersion = libAndroid.versions.plugin.kotlin.get()
    coreLibrariesVersion = kotlinVersion
}

android {
    namespace = "net.bi4vmr.tool.android.ui.universal"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    api(libAndroid.appcompat)
    api(libAndroid.ktx.core)
    api(libAndroid.annotation)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
