package net.bi4vmr.tool.android.platform.automotive.app

/**
 * AAOS媒体类型。
 *
 * AAOS应用可以在 `automotive_app_desc.xml` 中声明自己支持的媒体类型，本枚举类列出受支持的类型字符串。
 *
 * @author bi4vmr@outlook.com
 */
enum class AAOSAppType(

    /**
     * 配置文件标准名称。
     */
    val text: String
) {
    /**
     * 媒体应用，通常是音乐、广播等音频内容提供者。
     */
    MEDIA("media"),

    /**
     * 视频应用，通常是视频播放器、流媒体等视频内容提供者。
     */
    VIDEO("video");

    companion object {

        /**
         * 根据输入文本查找对应的枚举。
         *
         * @param[input] 输入文本。
         * @return 与输入文本匹配的枚举。如果未找到则返回空值。
         */
        @JvmStatic
        fun fromText(input: String): AAOSAppType? {
            entries.forEach {
                if (input.equals(it.text, ignoreCase = true)) {
                    return it
                }
            }

            return null
        }
    }
}
