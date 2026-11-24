@file:Suppress("UnstableApiUsage")

// 相关接口自 API 30 开始提供，因此本库只能用在最低 API ≥ 30 的项目中。
val versionMinSDK = 30
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val mvnGroupID: String = "net.bi4vmr.tool.android"
val mvnArtifactID: String = "ability-privacymonitor"
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
    namespace = "net.bi4vmr.tool.android.ability.privacymonitor"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    /* 工程组件 */
    compileOnly(project(":lib_ability:framework"))
    runtimeOnly(privateLibAndroid.ability.framework)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
