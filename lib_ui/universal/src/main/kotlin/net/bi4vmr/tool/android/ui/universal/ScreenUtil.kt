package net.bi4vmr.tool.android.ui.universal

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.roundToInt

/**
 * UI界面通用工具。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
object ScreenUtil {

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
}
