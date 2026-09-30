package net.bi4vmr.tool.android.ui.universal

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

/**
 * 扩展函数：APP资源。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */


/**
 * 获取指定的Drawable。
 *
 * @param[id] 资源ID。
 * @param[theme] 主题。默认为空值。
 * @return Drawable实例。
 * @throws[Resources.NotFoundException] 如果未找到资源则抛出该异常。
 */
fun Context.requireDrawable(@DrawableRes id: Int, theme: Theme? = null): Drawable {
    val drawable = ResourcesCompat.getDrawable(resources, id, theme ?: getTheme())
    if (drawable == null) {
        throw Resources.NotFoundException("Drawable resource not found! ID:[$id]")
    }

    return drawable
}

/**
 * 获取指定的Drawable。
 *
 * @param[id] 资源ID。
 * @param[theme] 主题。默认为空值。
 * @return Drawable实例。如果未找到资源则返回空值。
 */
fun Context.getDrawable(@DrawableRes id: Int, theme: Theme? = null): Drawable? {
    return try {
        ResourcesCompat.getDrawable(resources, id, theme ?: getTheme())
    } catch (e: Exception) {
        Log.e("BaseLib", "Get drawable with ID:[$id] failed! Reason:[${e.message}]")
        null
    }
}
