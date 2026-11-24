package net.bi4vmr.tool.android.ability.framework.appops

/**
 * OP Mode 枚举。
 *
 * AppOpsManager 中 OP Mode 的映射。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
enum class OpMode(

    /**
     * 数字编号。
     */
    val code: Int
) {

    ALLOWED(0),
    IGNORED(1),
    ERRORED(2),
    DEFAULT(3),
    FOREGROUND(4);

    companion object {

        /**
         * 根据编码查找对应的 Mode 枚举。
         *
         * @param[code] Mode 编码。
         * @return Mode 枚举。
         */
        @JvmStatic
        fun valueOf(code: Int): OpMode? {
            entries.forEach {
                if (it.code == code) {
                    return it
                }
            }

            return null
        }
    }

    /**
     * 获取字符串名称。
     *
     * @return 字符串名称。
     */
    fun toText(): String {
        return "MODE_$name"
    }
}
