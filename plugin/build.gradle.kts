// Gradle插件声明
plugins {
    `kotlin-dsl`
}

repositories {
    // 腾讯云仓库镜像：Maven中心仓库+Spring+Google+JCenter
    maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
    // 阿里云仓库镜像：Gradle社区插件
    maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin/") }
    // 阿里云仓库镜像：Maven中心仓库+JCenter
    maven { setUrl("https://maven.aliyun.com/repository/public/") }
    // 阿里云仓库镜像：Google仓库
    maven { setUrl("https://maven.aliyun.com/repository/google/") }

    gradlePluginPortal()
    mavenCentral()
    google()
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
    }
}
