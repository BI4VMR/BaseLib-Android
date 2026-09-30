package net.bi4vmr.tool.android.ui.universal

import android.view.View

/**
 * 扩展函数：View
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */


/**
 * 将View的可见性设为 [View.VISIBLE] 。
 */
fun View.toVisible() {
    visibility = View.VISIBLE
}

/**
 * 将View的可见性设为 [View.INVISIBLE] 。
 */
fun View.toInvisible() {
    visibility = View.INVISIBLE
}

/**
 * 将View的可见性设为 [View.GONE] 。
 */
fun View.toGone() {
    visibility = View.GONE
}
