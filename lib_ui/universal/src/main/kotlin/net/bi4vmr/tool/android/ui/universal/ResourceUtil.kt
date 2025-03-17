package net.bi4vmr.tool.android.ui.universal

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.Log
import androidx.annotation.AttrRes

/**
 * APP资源相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ResourceUtil {

    private const val TAG: String = "BaseLib-ResourceUtil"

    /**
     * 获取主题属性对应的颜色。
     *
     * 属性的值不存在或无法解析为色值时，将会抛出异常。
     *
     * @param[context] 上下文。
     * @param[attrID] 属性ID。
     * @return 色值。
     */
    @JvmStatic
    fun parseColorFromAttrUnsafe(context: Context, @AttrRes attrID: Int): Int {
        val ta: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(attrID))
        // 此处的"defValue"永不生效，如果资源无法解析为颜色，将会抛出"UnsupportedOperationException"异常。
        val color: Int = ta.getColor(0, Color.BLACK)
        ta.recycle()
        return color
    }

    /**
     * 获取主题属性对应的颜色。
     *
     * @param[context] 上下文。
     * @param[attrID] 属性ID。
     * @param[fallback] 操作失败时返回的色值，默认为黑色。
     * @return 色值。
     */
    @JvmStatic
    @JvmOverloads
    fun parseColorFromAttr(context: Context, @AttrRes attrID: Int, fallback: Int = Color.BLACK): Int {
        var ta: TypedArray? = null
        var color: Int = fallback
        try {
            ta = context.theme.obtainStyledAttributes(intArrayOf(attrID))
            color = ta.getColor(0, fallback)
        } catch (e: Exception) {
            Log.e(TAG, "Get color attribute [$attrID] failed!", e)
        } finally {
            ta?.recycle()
        }

        return color
    }
}
