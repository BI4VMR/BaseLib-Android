// Gradle 插件声明
plugins {
    alias(libAndroid.plugins.application).apply(false)
    alias(libAndroid.plugins.library).apply(false)
    alias(libAndroid.plugins.kotlin).apply(false)

    alias(privateLibJava.plugins.repo.private).apply(false)
    alias(privateLibJava.plugins.repo.public).apply(false)
}


val pluginMavenRepoPrivate: String = privateLibJava.plugins.repo.private.get().pluginId
val pluginMavenRepoPublic: String = privateLibJava.plugins.repo.public.get().pluginId

// 为子工程应用自定义插件
subprojects {
    project.pluginManager.apply(pluginMavenRepoPrivate)
    project.pluginManager.apply(pluginMavenRepoPublic)
}
