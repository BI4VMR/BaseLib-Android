@file:Suppress("UnstableApiUsage")

val versionMinSDK: Int = Integer.valueOf(agp.versions.minSdk.get())
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.test.universal.get()
val mvnGroupID: String = requireNotNull(depInTOML.group)
val mvnArtifactID: String = depInTOML.name
val mvnVersion: String = requireNotNull(depInTOML.version)

plugins {
    alias(libAndroid.plugins.library)

    alias(privateLibJava.plugins.java.version)
    alias(privateLibJava.plugins.publish.private)
}

android {
    namespace = "net.bi4vmr.tool.android.test.universal"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

    buildFeatures {
        buildConfig = false
    }
}

dependencies {
    api(libJava.junit4)
    api(libKotlin.ktx.coroutines.test)
    api(libKotlin.mockk)
    api(libAndroid.annotation)
    api(libAndroid.robolectric.core)
    api(libAndroid.robolectric.shadows.framework)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
