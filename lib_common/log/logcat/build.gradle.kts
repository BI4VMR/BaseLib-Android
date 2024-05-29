plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
}

val versionMinSDK: Int = Integer.valueOf(agp.versions.minSdk.get())
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

@Suppress("UnstableApiUsage")
android {
    namespace = "net.bi4vmr.tool.android.common.log.logcat"
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
    implementation(libs.android.appcompat)
    implementation(libs.android.corektx)
}
