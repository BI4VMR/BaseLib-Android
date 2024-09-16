@file:Suppress("UnstableApiUsage")

// 构建工具的依赖配置
pluginManagement {
    // 声明Gradle插件仓库
    repositories {
        // 添加本地私有仓库与代理镜像，无法直连时应当禁用该配置。
        val hostName: String = java.net.InetAddress.getLocalHost().hostName
        println("Current host name is [$hostName]")
        var isInPrivateLAN = false
        run {
            java.net.NetworkInterface.getNetworkInterfaces().toList().forEach {
                it.inetAddresses.toList().forEach { addr ->
                    if ((addr is java.net.Inet4Address) && (addr.hostAddress.startsWith("172.18."))) {
                        isInPrivateLAN = true
                        return@run
                    }
                }
            }
        }
        println("Current host in private LAN? [$isInPrivateLAN]")

        if (hostName.startsWith("BI4VMR") && isInPrivateLAN) {
            println("Current host is in private network, add LAN repositorys.")
            maven {
                isAllowInsecureProtocol = true
                setUrl("http://172.18.5.1:8081/repository/maven-union/")
            }
        } else {
            if (java.net.InetAddress.getByName("192.168.128.1").isReachable(2000)) {
                println("Current host is not in private network, add VPN repositorys.")
                maven {
                    isAllowInsecureProtocol = true
                    setUrl("http://192.168.128.1:8081/repository/maven-union/")
                }
            } else {
                println("Current host is not in private network, add LOCAL repositorys.")
                mavenLocal()
            }
        }

        // 腾讯云仓库镜像：Maven中心仓库
        maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }
        // 阿里云仓库镜像：Gradle社区插件
        maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin/") }

        mavenCentral()
        gradlePluginPortal()
    }
}

// 所有模块的依赖配置
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    // 声明Maven组件仓库
    repositories {
        // 添加本地私有仓库与代理镜像，无法直连时应当禁用该配置。
        val hostName: String = java.net.InetAddress.getLocalHost().hostName
        var isInPrivateLAN = false
        run {
            java.net.NetworkInterface.getNetworkInterfaces().toList().forEach {
                it.inetAddresses.toList().forEach { addr ->
                    if ((addr is java.net.Inet4Address) && (addr.hostAddress.startsWith("172.18."))) {
                        isInPrivateLAN = true
                        return@run
                    }
                }
            }
        }

        if (hostName.startsWith("BI4VMR") && isInPrivateLAN) {
            maven {
                isAllowInsecureProtocol = true
                setUrl("http://172.18.5.1:8081/repository/maven-union/")
            }
        } else {
            if (java.net.InetAddress.getByName("192.168.128.1").isReachable(2000)) {
                maven {
                    isAllowInsecureProtocol = true
                    setUrl("http://192.168.128.1:8081/repository/maven-union/")
                }
            } else {
                mavenLocal()
            }
        }

        // 腾讯云仓库镜像：Maven中心仓库
        maven { setUrl("https://mirrors.cloud.tencent.com/nexus/repository/maven-public/") }

        mavenCentral()
    }

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


// ----- 依赖传递 -----
// include(":lib_all")

// ----- 通用组件 -----
// Logcat扩展
include(":lib_common:logcat")

// ----- 用户界面 -----
// 通用工具
include(":lib_ui:universal")
// 模拟时钟
include(":lib_ui:analogclock")

// ----- 核心能力 -----
// 敏感权限监控工具
include(":lib_ability:privacymonitor")
// Framework扩展
include(":lib_ability:frameworkextend")


// ----- 示例程序 -----
// 通用组件 - Logcat扩展
include(":demo_common:logcat")

// 用户界面 - 通用工具
include(":demo_ui:universal")

// 系统能力 - 敏感权限监控工具
include(":demo_ability:privacymonitor")
