package net.bi4vmr.tool.android.ability.privacymonitor

/**
 * 本工具内置的监视器类型。
 *
 * 便于UI层封装权限图标等控件时使用。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
enum class PrivacyMonitorType {

    /**
     * 位置权限。
     */
    LOCATION,

    /**
     * 录音权限。
     */
    MICROPHONE,

    /**
     * 录像权限。
     */
    CAMERA;

    companion object {

        /**
         * 根据序号查找对应的监视器类型。
         *
         * @param[ordinal] 枚举序号。
         * @return 监视器类型。
         */
        @JvmStatic
        fun valueOf(ordinal: Int): PrivacyMonitorType? {
            entries.forEach {
                if (it.ordinal == ordinal) {
                    return it
                }
            }

            return null
        }
    }

    /**
     * 获取指定类型监视器的实例。
     *
     * @return 当前枚举对应的[PrivacyMonitor]对象。
     */
    fun newInstance(): PrivacyMonitor {
        return when (this) {
            LOCATION -> LocationPrivacyMonitor()
            MICROPHONE -> MICPrivacyMonitor()
            CAMERA -> CameraPrivacyMonitor()
        }
    }
}
