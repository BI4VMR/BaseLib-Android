@file:Suppress("UnstableApiUsage")

val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.common.logcat.get()
val mvnGroupID: String = requireNotNull(depInTOML.group)
val mvnArtifactID: String = depInTOML.name
val mvnVersion: String = requireNotNull(depInTOML.version)

plugins {
    alias(libAndroid.plugins.library)

    alias(privateLibJava.plugins.java.version)
    alias(privateLibJava.plugins.publish.private)
}

android {
    namespace = "net.bi4vmr.tool.android.common.logcat"
    compileSdk = versionCompileSDK

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    api(libKotlin.ktx.coroutines.core)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
