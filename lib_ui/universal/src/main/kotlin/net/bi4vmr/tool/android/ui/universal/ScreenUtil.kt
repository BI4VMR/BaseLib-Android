package net.bi4vmr.tool.android.ui.universal

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import kotlin.math.roundToInt

/**
 * 屏幕相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ScreenUtil {

    /**
     * 获取屏幕宽度。
     *
     * 默认使用主屏幕的参数。
     *
     * @return 屏幕宽度数值（单位：像素）。
     */
    @JvmStatic
    fun getScreenWidth(): Int {
        // 获取主屏幕的参数
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return dm.widthPixels
    }

    /**
     * 获取屏幕宽度。
     *
     * @param[context] 环境。
     * @return 屏幕宽度数值（单位：像素）。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    @JvmStatic
    fun getScreenWidth(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val dm = DisplayMetrics()
            context.display?.getRealMetrics(dm) ?: throw IllegalStateException("Context not contain display info!")
            dm.widthPixels
        } else {
            val wm: WindowManager = context.getSystemService(WindowManager::class.java)
            wm.defaultDisplay.width
        }
    }

    /**
     * 获取屏幕高度。
     *
     * 默认使用主屏幕的参数。
     *
     * @return 屏幕高度数值（单位：像素）。
     */
    @JvmStatic
    fun getScreenHeight(): Int {
        // 获取主屏幕的参数
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return dm.heightPixels
    }

    /**
     * 获取屏幕高度。
     *
     * @param[context] 环境。
     * @return 屏幕高度数值（单位：像素）。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    @JvmStatic
    fun getScreenHeight(context: Context): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val dm = DisplayMetrics()
            context.display?.getRealMetrics(dm) ?: throw IllegalStateException("Context not contain display info!")
            dm.heightPixels
        } else {
            val wm: WindowManager = context.getSystemService(WindowManager::class.java)
            wm.defaultDisplay.height
        }
    }

    /**
     * 将DP转为PX。
     *
     * 默认使用主屏幕的参数。
     *
     * @param[dpValue] DP数值。
     * @return PX数值。
     */
    @JvmStatic
    fun dpToPX(dpValue: Int): Int {
        // 获取主屏幕的参数
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return dpToPX(dm, dpValue.toFloat())
    }

    /**
     * 将DP转为PX。
     *
     * 默认使用主屏幕的参数。
     *
     * @param[dpValue] DP数值。
     * @return PX数值。
     */
    @JvmStatic
    fun dpToPX(dpValue: Float): Int {
        // 获取主屏幕的参数
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return dpToPX(dm, dpValue)
    }

    /**
     * 将DP转为PX。
     *
     * 可以指定屏幕所在的环境，以便支持多屏环境。
     *
     * @param[context] 环境。
     * @param[dpValue] DP数值。
     * @return PX数值。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    @JvmStatic
    fun dpToPX(context: Context, dpValue: Float): Int {
        return dpToPX(getDisplayMetrics(context), dpValue)
    }

    /**
     * 将DP转为PX。
     *
     * 可以指定屏幕参数，以便支持多屏环境。
     *
     * @param[dm] 屏幕参数。
     * @param[dpValue] DP数值。
     * @return PX数值。
     */
    @JvmStatic
    fun dpToPX(dm: DisplayMetrics, dpValue: Float): Int {
        // "applyDimension()"方法用于将指定的单位转换为像素
        val rawValue: Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, dm)
        // 物理像素不可能为小数，因此保留整数部分即可。
        return rawValue.roundToInt()
    }

    /**
     * 将SP转为PX。
     *
     * 默认使用主屏幕的参数。
     *
     * @param[spValue] SP数值。
     * @return PX数值。
     */
    @JvmStatic
    fun spToPX(spValue: Int): Int {
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return spToPX(dm, spValue.toFloat())
    }

    /**
     * 将SP转为PX。
     *
     * 默认使用主屏幕的参数。
     *
     * @param[spValue] SP数值。
     * @return PX数值。
     */
    @JvmStatic
    fun spToPX(spValue: Float): Int {
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return spToPX(dm, spValue)
    }

    /**
     * 将SP转为PX。
     *
     * 可以指定屏幕所在的环境，以便支持多屏环境。
     *
     * @param[context] 环境。
     * @param[spValue] SP数值。
     * @return PX数值。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    @JvmStatic
    fun spToPX(context: Context, spValue: Float): Int {
        return spToPX(getDisplayMetrics(context), spValue)
    }

    /**
     * 将SP转为PX。
     *
     * 可以指定屏幕参数，以便支持多屏环境。
     *
     * @param[dm] 屏幕参数。
     * @param[spValue] SP数值。
     * @return PX数值。
     */
    @JvmStatic
    fun spToPX(dm: DisplayMetrics, spValue: Float): Int {
        val rawValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, dm)
        return rawValue.roundToInt()
    }

    /**
     * 将PX转为DP。
     *
     * 默认使用主屏幕的参数。
     *
     * @param[pxValue] PX数值。
     * @return DP数值。
     */
    @JvmStatic
    fun pxToDP(pxValue: Int): Int {
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return pxToDP(dm, pxValue)
    }

    /**
     * 将PX转为DP。
     *
     * 可以指定屏幕所在的环境，以便支持多屏环境。
     *
     * @param[context] 环境。
     * @param[pxValue] PX数值。
     * @return DP数值。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    @JvmStatic
    fun pxToDP(context: Context, pxValue: Int): Int {
        return pxToDP(getDisplayMetrics(context), pxValue)
    }

    /**
     * 将PX转为DP。
     *
     * 可以指定屏幕参数，以便支持多屏环境。
     *
     * @param[dm] 屏幕参数。
     * @param[pxValue] PX数值。
     * @return DP数值。
     */
    @JvmStatic
    fun pxToDP(dm: DisplayMetrics, pxValue: Int): Int {
        val density = dm.density
        return (pxValue / density).roundToInt()
    }

    /**
     * 将PX转为SP。
     *
     * 默认使用主屏幕的参数。
     *
     * @param[pxValue] PX数值。
     * @return SP数值。
     */
    @JvmStatic
    fun pxToSP(pxValue: Int): Int {
        val dm: DisplayMetrics = Resources.getSystem().displayMetrics
        return pxToSP(dm, pxValue)
    }

    /**
     * 将PX转为SP。
     *
     * 可以指定屏幕所在的环境，以便支持多屏环境。
     *
     * @param[context] 环境。
     * @param[pxValue] PX数值。
     * @return SP数值。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    @JvmStatic
    fun pxToSP(context: Context, pxValue: Int): Int {
        return pxToSP(getDisplayMetrics(context), pxValue)
    }

    /**
     * 将PX转为SP。
     *
     * 可以指定屏幕参数，以便支持多屏环境。
     *
     * @param[dm] 屏幕参数。
     * @param[pxValue] PX数值。
     * @return SP数值。
     */
    @JvmStatic
    fun pxToSP(dm: DisplayMetrics, pxValue: Int): Int {
        val density = dm.scaledDensity
        return (pxValue / density).roundToInt()
    }

    /**
     * 获取指定Context对应的屏幕参数。
     *
     * @param[context] 环境。
     * @return DisplayMetrics对象。
     * @exception[IllegalStateException] Context不包含显示器相关信息。
     */
    private fun getDisplayMetrics(context: Context): DisplayMetrics {
        val dm = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            context.display?.getRealMetrics(dm) ?: throw IllegalStateException("Context not contain display info!")
        } else {
            val wm: WindowManager = context.getSystemService(WindowManager::class.java)
            wm.defaultDisplay.getRealMetrics(dm)
        }

        return dm
    }
}
