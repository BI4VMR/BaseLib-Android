package net.bi4vmr.tool.android.common.logcat

/**
 * 预设配置。
 *
 * @author bi4vmr@outlook.com
 */
object LogConfigs {

    /**
     * 预设配置：调试版本。
     *
     * 起始日志级别：Debug
     *
     * 类名与行号显示开关：开启
     *
     * 方法名称显示开关：开启
     *
     * 超长消息自动分行输出：开启
     */
    @JvmStatic
    fun debug() {
        LogConfig.minLevel = Level.DEBUG
        LogConfig.showClassInfo = true
        LogConfig.showMethodInfo = true
        LogConfig.multiLinePrint = true
    }

    /**
     * 预设配置：发布版本。
     *
     * 起始日志级别：Info
     *
     * 类名与行号显示开关：关闭
     *
     * 方法名称显示开关：关闭
     *
     * 超长消息自动分行输出：关闭
     */
    @JvmStatic
    fun release() {
        LogConfig.minLevel = Level.INFO
        LogConfig.showClassInfo = false
        LogConfig.showMethodInfo = false
        LogConfig.multiLinePrint = false
    }
}
