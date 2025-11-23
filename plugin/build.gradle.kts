// Gradle插件声明
plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        /* 公共Maven仓库 */
        create("PublicRepo") {
            // 声明插件ID
            id = "net.bi4vmr.gradle.plugin.repo.public"
            // 声明入口类
            implementationClass = "net.bi4vmr.gradle.plugin.PublicRepoPlugin"
        }

        /* 私有Maven仓库 */
        create("PrivateRepo") {
            id = "net.bi4vmr.gradle.plugin.repo.private"
            implementationClass = "net.bi4vmr.gradle.plugin.PrivateRepoPlugin"
        }

        /* 私有Maven Publish配置 */
        create("PrivatePublish") {
            id = "net.bi4vmr.gradle.plugin.maven.publish"
            implementationClass = "net.bi4vmr.gradle.plugin.PrivatePublishPlugin"
        }
    }

    sourceSets {
        getByName("main") {
            java {
                java.srcDir("src/main/kotlin")
            }
        }
    }
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.1.4")
}
