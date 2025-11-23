@file:Suppress("UnstableApiUsage")

// 相关接口自API 30开始提供，因此本库只能用在最低API大于或等于30的项目中。
val versionMinSDK = 30
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val mvnGroupID: String = "net.bi4vmr.tool.android"
val mvnArtifactID: String = "ability-privacymonitor"
val mvnVersion: String = "1.0.0"

plugins {
    alias(libAndroid.plugins.library)
    alias(libAndroid.plugins.kotlin)
    id(privateLibJava.plugins.repo.public.get().pluginId)
    id(privateLibJava.plugins.repo.private.get().pluginId)
    id(privateLibJava.plugins.publish.private.get().pluginId)
}

android {
    namespace = "net.bi4vmr.tool.android.ability.privacymonitor"
    compileSdk = versionCompileSDK

    defaultConfig {
        minSdk = versionMinSDK
    }

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
    // 内部组件依赖
    compileOnly(project(":lib_ability:framework"))
    runtimeOnly(privateLibAndroid.ability.framework)
}

privatePublishConfig {
    groupID = mvnGroupID
    artifactID = mvnArtifactID
    version = mvnVersion
}
