package net.bi4vmr.tool.android.ui.universal

import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

/**
 * 扩展函数：APP资源。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */

fun Context.requireDrawable(@DrawableRes id: Int, theme: Theme? = null): Drawable {
    val drawable = ResourcesCompat.getDrawable(resources, id, theme ?: getTheme())
    if (drawable == null) {
        throw Resources.NotFoundException("Drawable resource not found! ID:[$id]")
    }

    return drawable
}
