package net.bi4vmr.tool.android.ability.framework.util

import android.util.Log

/**
 * 日志工具。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
object FrameworkLog {

    /**
     * 日志输出全局开关。
     */
    var enable: Boolean = true

    /**
     * 调试日志输出开关。
     *
     * 仅当[enable]配置项为"true"时有意义。
     */
    var debug: Boolean = false

    /**
     * 普通日志输出开关。
     *
     * 仅当[enable]配置项为"true"时有意义。
     */
    var info: Boolean = true

    /**
     * 警告日志输出开关。
     *
     * 仅当[enable]配置项为"true"时有意义。
     */
    var warning: Boolean = true

    /**
     * 错误日志输出开关。
     *
     * 仅当[enable]配置项为"true"时有意义。
     */
    var error: Boolean = true

    /**
     * 输出调试日志。
     *
     * 仅供模块内部使用。
     *
     * @param[tag] 标签。
     * @param[content] 内容。
     */
    internal fun logD(tag: String, content: String) {
        if (isDebugOn()) {
            Log.d(tag, content)
        }
    }

    /**
     * 是否输出调试日志。
     *
     * @return 开关状态。
     */
    private fun isDebugOn(): Boolean {
        return enable && debug
    }

    /**
     * 输出普通日志。
     *
     * 仅供模块内部使用。
     *
     * @param[tag] 标签。
     * @param[content] 内容。
     */
    internal fun logI(tag: String, content: String) {
        if (isInfoOn()) {
            Log.i(tag, content)
        }
    }

    /**
     * 是否输出普通日志。
     *
     * @return 开关状态。
     */
    private fun isInfoOn(): Boolean {
        return enable && info
    }
    /**
     * 输出警告日志。
     *
     * 仅供模块内部使用。
     *
     * @param[tag] 标签。
     * @param[content] 内容。
     */
    internal fun logW(tag: String, content: String, exception: Exception? = null) {
        if (isWarningOn()) {
            Log.w(tag, content)
            exception?.printStackTrace()
        }
    }

    /**
     * 是否输出警告日志。
     *
     * @return 开关状态。
     */
    private fun isWarningOn(): Boolean {
        return enable && warning
    }

    /**
     * 输出错误日志。
     *
     * 仅供模块内部使用。
     *
     * @param[tag] 标签。
     * @param[content] 内容。
     * @param[exception] 异常。
     */
    internal fun logE(tag: String, content: String, exception: Exception? = null) {
        if (isErrorOn()) {
            Log.e(tag, content)
            exception?.printStackTrace()
        }
    }

    /**
     * 是否输出错误日志。
     *
     * @return 开关状态。
     */
    private fun isErrorOn(): Boolean {
        return enable && error
    }
}
