@file:Suppress("UnstableApiUsage")

// 相关接口自API 31开始提供，因此本库只能用在最低API大于或等于31的项目中。
val versionMinSDK = 31
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.ui.crossWindowBlurTool.get()
val mvnGroupID: String = requireNotNull(depInTOML.group)
val mvnArtifactID: String = depInTOML.name
val mvnVersion: String = requireNotNull(depInTOML.version)

plugins {
    alias(libAndroid.plugins.library)
    alias(libAndroid.plugins.kotlin)
    id(privateLibJava.plugins.repo.public.get().pluginId)
    id(privateLibJava.plugins.repo.private.get().pluginId)
    id(privateLibJava.plugins.publish.private.get().pluginId)
}

android {
    namespace = "net.bi4vmr.tool.android.ui.crosswindowblurtool"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    compileOnly(project(":lib_ability:framework_fakeimplementation"))

    implementation(libAndroid.ktx.core)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
