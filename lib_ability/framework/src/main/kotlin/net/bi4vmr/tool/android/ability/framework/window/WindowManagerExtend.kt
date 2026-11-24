package net.bi4vmr.tool.android.ability.framework.window

import android.view.WindowManager
import net.bi4vmr.tool.kotlin.common.reflect.requireFieldValue
import net.bi4vmr.tool.kotlin.common.reflect.setFieldValue

/**
 * [WindowManager] 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object WindowManagerExtend {

    /**
     * LayoutParams 的 Flags。
     */
    object Flags {
        /**
         * 详见 [WindowManager.LayoutParams.FLAG_FULLSCREEN]。
         */
        const val FULLSCREEN: Int = 0x00000400

        /**
         * 详见 [WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR]。
         */
        const val LAYOUT_INSET_DECOR: Int = 0x00010000
    }

    /**
     * LayoutParams 的 PrivateFlags。
     */
    object PrivateFlags {

        /**
         * 详见 `WindowManager.LayoutParams.PRIVATE_FLAG_TRUSTED_OVERLAY`。
         */
        const val TRUSTED_OVERLAY: Int = 0x20000000
    }


    private const val TAG: String = "WindowManagerExtend"


    /**
     * 获取私有 Flags。
     *
     * @param[lp] LayoutParams 实例。
     * @return 私有 Flags。
     */
    fun getPrivateFlags(lp: WindowManager.LayoutParams): Int =
        lp.requireFieldValue("privateFlags")

    /**
     * 设置私有 Flags。
     *
     * @param[lp] LayoutParams 实例。
     * @param[flags] 私有 Flags。
     */
    fun setPrivateFlags(lp: WindowManager.LayoutParams, flags: Int) =
        lp.setFieldValue("privateFlags", flags)

    /**
     * 添加私有 Flags。
     *
     * @param[lp] LayoutParams 实例。
     * @param[flags] 私有 Flags。
     */
    fun addPrivateFlags(lp: WindowManager.LayoutParams, flags: Int) {
        val currentFlags: Int = getPrivateFlags(lp)
        val newFlags: Int = currentFlags or flags
        setPrivateFlags(lp, newFlags)
    }
}
