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
    id("maven-publish")
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
    // 内部组件依赖
    compileOnly(project(":lib_ability:framework"))
    runtimeOnly(privateLibAndroid.ability.framework)
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

/**
 * 配置发布任务之间的依赖关系。
 *
 * 当前模块依赖某个模块，因此发布当前模块时，必须先发布被依赖的模块。
 *
 * "dependsOn()"方法指定了执行该任务时需要同时执行指定任务；"mustRunAfter()"方法则指定了该任务需要在指定任务完成后开始执行。
 */
tasks.named("publish") {
    val name = "publish"
    val depTasks: Array<String> = arrayOf(":lib_ability:framework:$name")

    dependsOn(*depTasks)
    mustRunAfter(*depTasks)
}

tasks.named("publishToMavenLocal") {
    val name = "publishToMavenLocal"
    val depTasks: Array<String> = arrayOf(":lib_ability:framework:$name")

    dependsOn(*depTasks)
    mustRunAfter(*depTasks)
}
