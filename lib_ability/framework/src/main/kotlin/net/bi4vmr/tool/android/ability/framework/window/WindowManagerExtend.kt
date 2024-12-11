package net.bi4vmr.tool.android.ability.framework.window

import android.util.Log
import android.view.WindowManager
import java.lang.reflect.Field

/**
 * [WindowManager]的功能扩展类。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object WindowManagerExtend {

    private val TAG: String = WindowManagerExtend::class.java.simpleName

    /**
     * LayoutParams的Flags。
     */
    object Flags {
        /**
         * 详见[WindowManager.LayoutParams.FLAG_FULLSCREEN]。
         */
        val FULLSCREEN: Int = 0x00000400

        /**
         * 详见[WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR]。
         */
        val LAYOUT_INSET_DECOR: Int = 0x00010000
    }

    /**
     * LayoutParams的PrivateFlags。
     */
    object PrivateFlags {
        /**
         * 详见 WindowManager.LayoutParams.PRIVATE_FLAG_TRUSTED_OVERLAY 。
         */
        val TRUSTED_OVERLAY: Int = 0x20000000
    }

    fun getPrivateFlags(lp: WindowManager.LayoutParams): Int? {
        var flags: Int? = null
        try {
            val field: Field = WindowManager.LayoutParams::class.java.getField("privateFlags")
            flags = field.getInt(lp)
        } catch (e: Exception) {
            Log.e(TAG, "Reflect operate failed! Reason:[${e.message}]")
            e.printStackTrace()
        }

        return flags
    }

    fun setPrivateFlags(lp: WindowManager.LayoutParams, flags: Int) {
        try {
            val field: Field = WindowManager.LayoutParams::class.java.getField("privateFlags")
            field.setInt(lp, flags)
        } catch (e: Exception) {
            Log.e(TAG, "Reflect operate failed! Reason:[${e.message}]")
            e.printStackTrace()
        }
    }

    fun addPrivateFlags(lp: WindowManager.LayoutParams, flags: Int) {
        val currentFlags: Int = getPrivateFlags(lp) ?: 0
        val newFlags: Int = currentFlags or flags
        setPrivateFlags(lp, newFlags)
    }
}
