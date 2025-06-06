@file:Suppress("UnstableApiUsage")

val versionMinSDK: Int = Integer.valueOf(agp.versions.minSdk.get())
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val mvnGroupID: String = "net.bi4vmr.tool.android"
val mvnArtifactID: String = "ui-universal"
val mvnVersion: String = "1.0.0"

plugins {
    alias(libAndroid.plugins.library)
    alias(libAndroid.plugins.kotlin)
    id("maven-publish")
}

android {
    namespace = "net.bi4vmr.tool.android.ui.universal"
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

    publishing {
        multipleVariants {
            // 指定以下配置对所有Build Variant生效
            allVariants()
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libAndroid.ktx.core)
    implementation(libAndroid.annotation)
}

publishing {
    repositories {
        // 私有仓库
        maven {
            name = "private"
            isAllowInsecureProtocol = true
            setUrl("http://172.16.5.1:8081/repository/maven-private/")
            credentials {
                username = "uploader"
                password = "uploader"
            }
        }
    }

    publications {
        // 创建名为"maven"的发布配置
        create<MavenPublication>("maven") {
            // 产物的基本信息
            groupId = mvnGroupID
            artifactId = mvnArtifactID
            version = mvnVersion

            // 在编译完成后，将"release"类型的产物作为程序包发布。
            afterEvaluate {
                from(components.getByName("release"))
            }

            // POM信息
            pom {
                name.set(mvnArtifactID)
                url.set("https://github.com/BI4VMR/BaseLib-Android")
                packaging = "aar"
                developers {
                    developer {
                        name.set("BI4VMR")
                        email.set("bi4vmr@outlook.com")
                    }
                }
            }
        }
    }
}
