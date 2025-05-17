// Gradle插件声明
plugins {
    alias(libAndroid.plugins.application) apply false
    alias(libAndroid.plugins.library) apply false
    alias(libAndroid.plugins.kotlin) apply false
    id("net.bi4vmr.gradle.plugin.repo.private") apply false
    id("net.bi4vmr.gradle.plugin.repo.public") apply false
}

// 为子工程应用自定义插件
allprojects {
    project.apply(plugin = "net.bi4vmr.gradle.plugin.repo.private")
    project.apply(plugin = "net.bi4vmr.gradle.plugin.repo.public")
}
