package net.bi4vmr.tool.android.common.logcat

import android.util.Log
import net.bi4vmr.tool.android.common.logcat.LogUtil.e

/**
 * 日志工具。
 *
 * Logcat功能扩展。
 *
 * @author bi4vmr@outlook.com
 */
object LogUtil {

    // 工具调试专用标签（非用户设置的标签！）。
    private const val TAG: String = "BaseLib-LogUtil"

    /**
     * 输出Verbose日志。
     *
     * @param[message] 消息内容。
     * @param[e] 异常。可选，不需要输出时可以填入空值。
     * @param[tag] 日志Tag。可选，默认为空值，此时自动侦测类名。
     */
    @JvmStatic
    @JvmOverloads
    fun v(message: Any, e: Throwable? = null, tag: String? = null) {
        print(Level.VERBOSE, message, tag, e = e)
    }

    /**
     * 输出Debug日志。
     *
     * @param[message] 消息内容。
     * @param[e] 异常。可选，不需要输出时可以填入空值。
     * @param[tag] 日志Tag。可选，默认为空值，此时自动侦测类名。
     */
    @JvmStatic
    @JvmOverloads
    fun d(message: Any, e: Throwable? = null, tag: String? = null) {
        print(Level.DEBUG, message, tag, e = e)
    }

    /**
     * 输出Info日志。
     *
     * @param[message] 消息内容。
     * @param[e] 异常。可选，不需要输出时可以填入空值。
     * @param[tag] 日志Tag。可选，默认为空值，此时自动侦测类名。
     */
    @JvmStatic
    @JvmOverloads
    fun i(message: Any, e: Throwable? = null, tag: String? = null) {
        print(Level.INFO, message, tag, e = e)
    }

    /**
     * 输出Warn日志。
     *
     * @param[message] 消息内容。
     * @param[e] 异常。可选，不需要输出时可以填入空值。
     * @param[tag] 日志Tag。可选，默认为空值，此时自动侦测类名。
     */
    @JvmStatic
    @JvmOverloads
    fun w(message: Any, e: Throwable? = null, tag: String? = null) {
        print(Level.WARN, message, tag, e = e)
    }

    /**
     * 输出Error日志。
     *
     * @param[message] 消息内容。
     * @param[e] 异常。可选，不需要输出时可以填入空值。
     * @param[tag] 日志Tag。可选，默认为空值，此时自动侦测类名。
     */
    @JvmStatic
    @JvmOverloads
    fun e(message: Any, e: Throwable? = null, tag: String? = null) {
        print(Level.ERROR, message, tag, e = e)
    }

    /**
     * 输出日志。
     *
     * 该方法为功能最多的日志输出方法，没有特殊需求时选择其他方法即可。
     *
     * @param[level] 日志级别。
     * @param[message] 日志内容。
     * @param[tag] 日志Tag。
     * @param[e] 异常，可选，不需要打印时默认为空值。
     *
     * 栈首的若干项为本工具类内部调用信息，它们可以被忽略，之后的元素为调用者信息。
     */
    fun print(level: Level, message: Any, tag: String? = null, e: Throwable? = null) {
        // 如果全局输出开关未开启，则不必输出日志。
        if (!LogConfig.enable) {
            return
        }

        // 如果日志级别低于允许输出的最小级别，则不必输出日志。
        if (level.ordinal < LogConfig.minLevel.ordinal) {
            return
        }

        // 获取调用栈
        val stack: Array<StackTraceElement> = Thread.currentThread().stackTrace

        // 工具调试日志
        // stack.forEach {
        //     val className: String = it.className
        //     val methodName: String = it.methodName
        //     val lineNumber: Int = it.lineNumber
        //     Log.d(TAG, "Class:[$className] Method:[$methodName] Line:[$lineNumber]")
        // }

        /* 构建输出内容 */
        val trace: StackTraceElement = searchUserCall(stack)
        val className: String = parseClassName(trace)
        val methodName: String = trace.methodName
        val lineNumber: Int = trace.lineNumber

        val tagPrefix: String = LogConfig.tagPrefix
        val printTag = if (tag == null) {
            /* Tag未指定，填入调用者的类名。 */
            "$tagPrefix-$className"
        } else if (tag.isEmpty()) {
            /* Tag为空字符串，只显示前缀。 */
            tagPrefix
        } else {
            "$tagPrefix-$tag"
        }

        val showClassInfo: Boolean = LogConfig.showClassInfo
        val showMethodInfo: Boolean = LogConfig.showMethodInfo
        val printmessage = if (showClassInfo && showMethodInfo) {
            "[$className:$lineNumber#$methodName] $message"
        } else if (showClassInfo) {
            "[$className:$lineNumber] $message"
        } else {
            message
        }

        printMultiLineIfNeeded(level, printTag, printmessage, e)
    }

    /**
     * 查找调用栈数组中的首个用户调用。
     *
     * @param[stack] 调用栈数组。
     * @return 调用栈信息。
     */
    private fun searchUserCall(stack: Array<StackTraceElement>): StackTraceElement {
        // 系统调用列表（全字匹配）
        val systemCallFull = arrayOf(
            "dalvik.system.VMStack",
            "java.lang.Thread",
            "net.bi4vmr.tool.android.common.logcat.LogUtil"
        )
        // 系统调用列表（模糊匹配）
        val systemCallObscure = arrayOf(
            "Synthetic"
        )

        var traceInfo = StackTraceElement("UnKnown", "UnKnown", "-", -1)
        for (ste: StackTraceElement in stack) {
            // 跳过系统调用
            if (ste.className in systemCallFull) {
                continue
            }

            for (obscureWord: String in systemCallObscure) {
                if (ste.className.contains(obscureWord, ignoreCase = true)) {
                    continue
                }
            }

            traceInfo = ste
            break
        }

        return traceInfo
    }

    /**
     * 解析类名。
     *
     * @param[trace] 调用栈。
     * @return 类名。
     */
    private fun parseClassName(trace: StackTraceElement): String {
        var className = "UnKnown"

        val fullName: String = trace.className
        val classNameIndex: Int = fullName.lastIndexOf('.')
        if (classNameIndex != -1) {
            className = fullName.substring(classNameIndex + 1)
        }

        // 如果类名包含内部类符号("$")，则需要移除内部类信息。
        val classNameEndIndex: Int = className.indexOf('$')
        if (classNameEndIndex != -1) {
            className = className.substring(0, classNameEndIndex)
        }

        return className
    }

    /**
     * 根据需要将超长日志分为多行输出。
     *
     * @param[level] 日志级别。
     * @param[tag] 日志Tag。
     * @param[message] 日志内容。
     * @param[e] 异常，可选，不需要输出时可以填入空值。
     */
    private fun printMultiLineIfNeeded(level: Level, tag: String, message: Any, e: Throwable? = null) {
        // 每行最大长度
        val limit = 1250

        // 如果消息是文本，直接用于输出；否则先调用 `toString()` 方法获取文本内容再输出。
        val text = message as? String ?: message.toString()

        // 未开启自动换行输出，或长度未达到限制数值，则原样输出。
        if (!LogConfig.multiLinePrint || text.length <= limit) {
            logcat(level, tag, text, e)
            return
        }

        // 计算切分后的行数（不含最后一行）
        val lines: Int = text.length / limit
        // 循环打印每一行内容
        for (i in 0..lines) {
            if (i != lines) {
                /* 打印完整的行 */
                val line: String = text.substring(i * limit, (i + 1) * limit)
                logcat(level, tag, "Line:[${i + 1}] $line", e)
            } else {
                /* 打印最后一行 */
                val line: String = text.substring(i * limit)
                if (line.isNotEmpty()) {
                    logcat(level, tag, "Line:[${i + 1}] $line", e)
                }
            }
        }
    }

    /**
     * 使用Logcat输出日志。
     *
     * @param[level] 日志级别。
     * @param[tag] 日志Tag。
     * @param[message] 日志内容。
     * @param[e] 异常。可选，不需要输出时可以填入空值。
     */
    private fun logcat(level: Level, tag: String, message: Any, e: Throwable? = null) {
        // 如果消息是文本，直接用于输出；否则先调用 `toString()` 方法获取文本内容再输出。
        val text = message as? String ?: message.toString()

        if (e != null) {
            when (level) {
                Level.VERBOSE -> Log.v(tag, text, e)
                Level.DEBUG -> Log.d(tag, text, e)
                Level.INFO -> Log.i(tag, text, e)
                Level.WARN -> Log.w(tag, text, e)
                Level.ERROR -> Log.e(tag, text, e)
            }
        } else {
            when (level) {
                Level.VERBOSE -> Log.v(tag, text)
                Level.DEBUG -> Log.d(tag, text)
                Level.INFO -> Log.i(tag, text)
                Level.WARN -> Log.w(tag, text)
                Level.ERROR -> Log.e(tag, text)
            }
        }
    }
}
