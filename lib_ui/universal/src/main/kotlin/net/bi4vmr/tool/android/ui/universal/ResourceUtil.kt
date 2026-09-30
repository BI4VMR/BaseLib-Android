package net.bi4vmr.tool.android.ui.universal

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.UiContext
import androidx.core.graphics.drawable.toDrawable

/**
 * APP资源相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ResourceUtil {

    private const val TAG: String = "BaseLib-ResourceUtil"

    /*
     * ----- 从主题属性中解析资源 -----
     */

    /**
     * 获取主题属性对应的颜色。
     *
     * 属性的值不存在或无法解析为色值时，将会抛出异常。
     *
     * @param[context] 上下文(UI)。
     * @param[attrID] 属性ID。
     * @return 色值。
     * @throws Resources.NotFoundException 当前主题没有声明该属性。
     * @throws UnsupportedOperationException 属性的值无法解析为色值。
     */
    @JvmStatic
    @Throws(Resources.NotFoundException::class, UnsupportedOperationException::class)
    fun parseColorFromAttrUnsafe(@UiContext context: Context, @AttrRes attrID: Int): Int {
        val ta: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(attrID))
        // 此处的"defValue"永不生效，如果资源无法解析为颜色，将会抛出"UnsupportedOperationException"异常。
        val color: Int = ta.getColor(0, Color.BLACK)
        ta.recycle()
        return color
    }

    /**
     * 获取主题属性对应的颜色。
     *
     * @param[context] 上下文(UI)。
     * @param[attrID] 属性ID。
     * @param[fallback] 操作失败时返回的色值，默认为黑色。
     * @return 色值。
     */
    @JvmStatic
    @JvmOverloads
    fun parseColorFromAttr(
        @UiContext context: Context,
        @AttrRes attrID: Int,
        @ColorInt fallback: Int = Color.BLACK
    ): Int {
        var color: Int = fallback

        try {
            color = parseColorFromAttrUnsafe(context, attrID)
        } catch (e: Exception) {
            Log.e(TAG, "Get color attribute [$attrID] failed!", e)
        }

        return color
    }

    /**
     * 获取主题属性对应的Drawable。
     *
     * 属性的值不存在或无法解析为Drawable时，将会抛出异常。
     *
     * @param[context] 上下文(UI)。
     * @param[attrID] 属性ID。
     * @return Drawable实例。
     * @throws Resources.NotFoundException 当前主题没有声明该属性。
     * @throws UnsupportedOperationException 属性的值无法解析为Drawable。
     */
    @JvmStatic
    @Throws(Resources.NotFoundException::class, UnsupportedOperationException::class)
    fun parseDrawableFromAttrUnsafe(@UiContext context: Context, @AttrRes attrID: Int): Drawable {
        val ta: TypedArray = context.theme.obtainStyledAttributes(intArrayOf(attrID))
        val drawable: Drawable? = ta.getDrawable(0)
        ta.recycle()
        if (drawable == null) {
            throw Resources.NotFoundException("Drawable attribute [$attrID] not found!")
        }
        return drawable
    }

    /**
     * 获取主题属性对应的Drawable。
     *
     * @param[context] 上下文(UI)。
     * @param[attrID] 属性ID。
     * @param[fallback] 操作失败时返回的Drawable，默认为黑色Drawable。
     * @return Drawable实例。
     */
    @JvmStatic
    @JvmOverloads
    fun parseDrawableFromAttr(
        @UiContext context: Context,
        @AttrRes attrID: Int,
        fallback: Drawable = Color.BLACK.toDrawable()
    ): Drawable {
        var drawable: Drawable = fallback

        try {
            drawable = parseDrawableFromAttrUnsafe(context, attrID)
        } catch (e: Exception) {
            Log.e(TAG, "Get drawable attribute [$attrID] failed!", e)
        }

        return drawable
    }
}
