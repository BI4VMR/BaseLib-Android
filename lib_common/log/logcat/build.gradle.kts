plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.android.kotlin)
    id("maven-publish")
}

val versionMinSDK: Int = Integer.valueOf(agp.versions.minSdk.get())
val versionCompileSDK: Int = Integer.valueOf(agp.versions.compileSdk.get())

val mvnGroupID: String = "net.bi4vmr.tool.android"
val mvnArtifactID: String = "common-log-logcat"
val mvnVersion: String = "1.1.0"

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
    implementation(libs.kotlinx.coroutines)
}

// 发布源码包的任务
val sourcesJar by tasks.creating(Jar::class) {
    // 为源码包添加后缀与字节码包作区分
    archiveClassifier.set("sources")
    // Android工程需要调用"android"模块中的"sourceSets"，直接书写"sourceSets"将会找不到"main"等集合。
    from(android.sourceSets.getByName("main").java.srcDirs)
}

publishing {
    repositories {
        // 私有仓库
        maven {
            name = "private"
            isAllowInsecureProtocol = true
            setUrl("http://172.18.5.1:8081/repository/maven-private/")
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

            // 将"bundleReleaseAar"任务产生的字节码包发布
            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
            // 发布源码包
            artifact(sourcesJar)

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

            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                // 获取当前模块的所有"implementation"节点
                configurations.getByName("implementation").allDependencies.forEach { dependency ->
                    println("Parse dependency item. Info:[$dependency]")
                    val dependencyNode = dependenciesNode.appendNode("dependency")
                    dependencyNode.appendNode("groupId", dependency.group)
                    dependencyNode.appendNode("artifactId", dependency.name)
                    dependencyNode.appendNode("version", dependency.version)
                    dependencyNode.appendNode("scope", "implementation")
                }
            }
        }
    }
}
