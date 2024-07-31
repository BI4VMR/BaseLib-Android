# 简介
Android基础工具库，包含日志输出、自定义控件、文件管理等常用工具。

# 使用方法
当我们进行测试时，可以在项目中依赖 `ALL` 模块，同时引用所有子模块。

```kotlin
dependencies {
    implementation("net.bi4vmr.tool.android:all:1.0.0")
}
```

# 通用工具
## 日志工具
### Logcat工具
目前已经支持的功能：

- 支持统一设置Tag前缀、全局输出开关、自定义起始输出级别。
- 支持在不填写Tag时自动解析调用者的类名作为Tag。
- 支持自动解析调用者的类名、代码行号、方法名称。
- 支持超长消息自动分行输出，防止被截断丢失信息。
- 支持周期性输出信息，例如：版本号、运行时长等。

示例：

```kotlin
// 设置Tag前缀
LogConfig.setTagPrefix("TestApp")

// 加载预设配置：调试版本
LogConfigs.debug()

// 输出日志，不指定Tag。
LogUtil.d("Message")
LogUtil.i("Message")

// 输出日志，指定Tag。
LogUtil.d("Tag", "Message")
LogUtil.i("Tag", "Message")
```

版本变更：

v1.0.0:

1. 实现基本功能。
