@file:Suppress("UnstableApiUsage")

// 相关接口自 API 31 开始提供，因此本库只能用在最低 API ≥ 31 的项目中。
val versionMinSDK = 31
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.ui.crossWindowBlurTool.get()
val mvnGroupID: String = requireNotNull(depInTOML.group)
val mvnArtifactID: String = depInTOML.name
val mvnVersion: String = requireNotNull(depInTOML.version)

plugins {
    alias(libAndroid.plugins.library)

    alias(privateLibJava.plugins.java.version)
    alias(privateLibJava.plugins.publish.private)
}

android {
    namespace = "net.bi4vmr.tool.android.ui.crosswindowblurtool"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    compileOnly(project(":lib_ability:framework_fakeimplementation"))

    api(libAndroid.ktx.core)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
