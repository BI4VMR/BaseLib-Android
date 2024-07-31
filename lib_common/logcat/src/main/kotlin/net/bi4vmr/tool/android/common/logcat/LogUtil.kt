package net.bi4vmr.tool.android.common.logcat

import android.util.Log

/**
 * 日志工具。
 *
 * Logcat功能扩展。
 *
 * @author bi4vmr@outlook.com
 */
object LogUtil {

    // 工具调试专用标签（非用户设置的标签！）。
    private val TAG: String = "BaseLib-${LogUtil::class.java.simpleName}"

    /**
     * 输出Verbose日志。
     *
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun v(msg: String) {
        print(Level.VERBOSE, msg)
    }

    /**
     * 输出Verbose日志。
     *
     * @param[tag] 消息Tag。
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun v(tag: String, msg: String) {
        print(Level.VERBOSE, msg, tag)
    }

    /**
     * 输出Debug日志。
     *
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun d(msg: String) {
        print(Level.DEBUG, msg)
    }

    /**
     * 输出Debug日志。
     *
     * @param[tag] 消息Tag。
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun d(tag: String, msg: String) {
        print(Level.DEBUG, msg, tag)
    }

    /**
     * 输出Info日志。
     *
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun i(msg: String) {
        print(Level.INFO, msg)
    }

    /**
     * 输出Info日志。
     *
     * @param[tag] 消息Tag。
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun i(tag: String, msg: String) {
        print(Level.INFO, msg, tag)
    }

    /**
     * 输出Warn日志。
     *
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun w(msg: String) {
        print(Level.WARN, msg)
    }

    /**
     * 输出Warn日志。
     *
     * @param[tag] 消息Tag。
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun w(tag: String, msg: String) {
        print(Level.WARN, msg, tag)
    }

    /**
     * 输出Error日志。
     *
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun e(msg: String) {
        print(Level.ERROR, msg)
    }

    /**
     * 输出Error日志。
     *
     * @param[tag] 消息Tag。
     * @param[msg] 消息内容。
     */
    @JvmStatic
    fun e(tag: String, msg: String) {
        print(Level.ERROR, msg, tag)
    }

    /**
     * 输出日志。
     *
     * 该方法为功能最多的日志输出方法，没有特殊需求时选择其他方法即可。
     *
     * @param[level] 日志级别。
     * @param[msg] 日志内容。
     * @param[tag] 日志Tag。
     * @param[stackOffset] 调用者信息在调用栈中的索引。
     *
     * 栈首的若干项为本工具类内部调用信息，它们可以被忽略，之后的元素为调用者信息。
     */
    fun print(level: Level, msg: String, tag: String? = null, stackOffset: Int = 5) {
        // 如果全局输出开关未开启，则不必输出日志。
        if (!LogConfig.enable) {
            return
        }

        // 如果日志级别低于允许输出的最小级别，则不必输出日志。
        if (level.ordinal < LogConfig.minLevel.ordinal) {
            return
        }

        // 获取调用栈。
        val stack: Array<StackTraceElement> = Thread.currentThread().stackTrace

        // 工具调试日志。
        // stack.forEach {
        //     val className: String = it.className
        //     val methodName: String = it.methodName
        //     val lineNumber: Int = it.lineNumber
        //     Log.d(TAG, "Class:[$className] Method:[$methodName] Line:[$lineNumber]")
        // }

        // 工具调试日志。
        // Log.d(TAG, "Called by JVM static function? [$callByJVMStatic]")

        /* 构建输出内容 */
        val className: String = parseClassName(stack, stackOffset)
        val methodName: String = parseMethodName(stack, stackOffset)
        val lineNumber: Int = stack[stackOffset].lineNumber

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
        val printMsg = if (showClassInfo && showMethodInfo) {
            "[$className:$lineNumber#$methodName] $msg"
        } else if (showClassInfo) {
            "[$className:$lineNumber] $msg"
        } else {
            msg
        }

        printMultiLineIfNeeded(level, printTag, printMsg)
    }

    /**
     * 解析类名。
     *
     * @param[stack] 调用栈。
     * @param[offset] 需要解析的元素索引。
     * @return 类名。
     */
    private fun parseClassName(stack: Array<StackTraceElement>, offset: Int): String {
        var className = "UnKnown"

        val fullName: String = stack[offset].className
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
     * 解析方法名称。
     *
     * @param[stack] 调用栈。
     * @param[offset] 需要解析的元素索引。
     * @return 类名。
     */
    private fun parseMethodName(stack: Array<StackTraceElement>, offset: Int): String {
        var callerMethodName: String = stack[offset].methodName

        /*
         * 判断本工具是否在Lambda表达式中被调用。
         *
         * 在普通方法中调用本工具时，调用方法名称可以在对应的栈元素中获取。
         * 在Lambda表达式中调用本工具时，需要遍历查找方法名称不含"lambda"的元素。
         */
        if (callerMethodName.contains("lambda$")) {
            for (i in offset + 1 until stack.size) {
                val name: String = stack[i].methodName
                if (!name.contains("lambda$")) {
                    callerMethodName = name
                    break
                }
            }
        }

        return callerMethodName
    }

    /**
     * 根据需要将超长日志分为多行输出。
     *
     * @param[level] 日志级别。
     * @param[tag] 日志Tag。
     * @param[message] 日志内容。
     */
    private fun printMultiLineIfNeeded(level: Level, tag: String, message: String) {
        // 每行最大长度
        val limit = 1250

        // 未开启自动换行输出，或长度未达到限制数值，则原样输出。
        if (!LogConfig.multiLinePrint || message.length <= limit) {
            logcat(level, tag, message)
            return
        }

        // 计算切分后的行数（不含最后一行）
        val lines: Int = message.length / limit
        // 循环打印每一行内容
        for (i in 0..lines) {
            if (i != lines) {
                /* 打印完整的行 */
                val line: String = message.substring(i * limit, (i + 1) * limit)
                logcat(level, tag, "Line:[${i + 1}] $line")
            } else {
                /* 打印最后一行 */
                val line: String = message.substring(i * limit)
                if (line.isNotEmpty()) {
                    logcat(level, tag, "Line:[${i + 1}] $line")
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
     */
    private fun logcat(level: Level, tag: String, message: String) {
        when (level) {
            Level.VERBOSE -> Log.v(tag, message)
            Level.DEBUG -> Log.d(tag, message)
            Level.INFO -> Log.i(tag, message)
            Level.WARN -> Log.w(tag, message)
            Level.ERROR -> Log.e(tag, message)
        }
    }
}
