@file:Suppress("UnstableApiUsage")

val versionMinSDK: Int = Integer.valueOf(agp.versions.minSdk.get())
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.ui.baseRVAdapter.get()
val mvnGroupID: String = "net.bi4vmr.tool.android"
val mvnArtifactID: String = "storage-room-tool"
val mvnVersion: String = "1.0.0"

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
    namespace = "net.bi4vmr.tool.android.storage.room"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    api(libAndroid.room.runtime)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
