@file:Suppress("UnstableApiUsage")

val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val depInTOML: MinimalExternalModuleDependency = privateLibAndroid.common.logcat.get()
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
    namespace = "net.bi4vmr.tool.android.common.logcat"
    compileSdk = versionCompileSDK

    sourceSets {
        getByName("main") {
            java {
                java.srcDirs("src/main/kotlin")
            }
        }
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
    implementation(libKotlin.ktx.coroutines.core)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
