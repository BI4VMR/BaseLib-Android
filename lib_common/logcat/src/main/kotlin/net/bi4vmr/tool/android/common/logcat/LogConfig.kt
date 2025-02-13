package net.bi4vmr.tool.android.common.logcat

/**
 * 日志工具功能配置。
 *
 * @author bi4vmr@outlook.com
 */
object LogConfig {

    // 日志输出全局开关
    @JvmStatic
    var enable: Boolean = true

    // 输出日志的起始级别
    @JvmStatic
    var minLevel: Level = Level.VERBOSE

    // Tag前缀
    @JvmStatic
    var tagPrefix: String = "LogUtil"

    // 类名与行号显示开关
    @JvmStatic
    var showClassInfo: Boolean = false

    // 方法名称显示开关
    @JvmStatic
    var showMethodInfo: Boolean = false

    // 超长消息自动分行输出
    @JvmStatic
    var multiLinePrint: Boolean = true
}
