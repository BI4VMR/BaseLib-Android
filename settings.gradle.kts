@file:Suppress("UnstableApiUsage")

// 构建工具的依赖配置
pluginManagement {
    // 声明Gradle插件仓库
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
}

// 所有模块的依赖配置
dependencyResolutionManagement {
    // 版本管理配置
    versionCatalogs {
        // 公共组件(Java)
        create("libJava") {
            from(files("misc/version/dependency_public_java.toml"))
        }

        // 公共组件(Kotlin)
        create("libKotlin") {
            from(files("misc/version/dependency_public_kotlin.toml"))
        }

        // 公共组件(Android)
        create("libAndroid") {
            from(files("misc/version/dependency_public_android.toml"))
        }

        // 私有组件(Java)
        create("privateLibJava") {
            from(files("misc/version/dependency_private_java.toml"))
        }

        // 私有组件(Android)
        create("privateLibAndroid") {
            from(files("misc/version/dependency_private_android.toml"))
        }

        // AGP相关配置
        create("agp") {
            from(files("misc/version/agp.toml"))
        }
    }
}

/* ----- 工程结构声明 ----- */
// 主工程名称
rootProject.name = "BaseLib-Android"
// 加载自定义插件
includeBuild("plugin")

/* ----- 通用组件 ----- */
// Logcat扩展
include(":lib_common:logcat")

/* ----- 用户界面 ----- */
// 通用工具
include(":lib_ui:universal")
// 模拟时钟
include(":lib_ui:analogclock")
// 饼图进度条
include(":lib_ui:pieprogressbar")
// RecyclerView通用适配器
include(":lib_ui:baservadapter")
// 跨窗口背景模糊工具
include(":lib_ui:crosswindowblurtool")

/* ----- 核心能力 ----- */
// 敏感权限监控工具
include(":lib_ability:privacymonitor")
// Framework扩展
include(":lib_ability:framework")
// Framework扩展（空实现）
include(":lib_ability:framework_fakeimplementation")

/* ----- 数据存储 ----- */
// Room工具
include(":lib_storage:room-tool")

/* ----- 示例程序 ----- */
// 通用组件 - Logcat扩展
include(":demo_common:logcat")

// 用户界面 - 通用工具
include(":demo_ui:universal")
// 用户界面 - RecyclerView通用适配器
include(":demo_ui:baservadapter")
include(":demo_ui:crosswindowblurtool")

// 系统能力 - 敏感权限监控工具
include(":demo_ability:privacymonitor")
