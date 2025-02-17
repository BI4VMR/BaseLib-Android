package net.bi4vmr.tool.android.ui.universal

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import androidx.annotation.AttrRes

/**
 * APP资源相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ResourceUtil {

    /**
     * 获取主题属性对应的颜色。
     *
     * @return 色值。
     */
    @JvmStatic
    fun getColorFromAttr(context: Context, @AttrRes attrID: Int): Int {
        var color: Int = Color.BLACK
        val ta: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(attrID))
        color = ta.getColor(0, Color.BLACK)
        ta.recycle()
        return color
    }
}
