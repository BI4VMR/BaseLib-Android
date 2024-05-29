# 简介
Android基础工具库，包含日志输出、自定义控件、文件管理等常用工具。

# 通用工具
## 日志工具
### Logcat工具
目前已经支持的功能：

- 全局输出开关
- 支持定义输出日志的起始级别
- 支持统一设置Tag前缀。
- 不填写Tag时自动解析调用者的类名作为Tag。
- 自动解析调用者的类名、代码行号、方法名称。
- 超长消息自动分行输出，防止被截断丢失信息。

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
