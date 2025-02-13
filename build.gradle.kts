// Gradle插件声明
plugins {
    alias(libAndroid.plugins.application) apply false
    alias(libAndroid.plugins.library) apply false
    alias(libAndroid.plugins.kotlin) apply false
}
