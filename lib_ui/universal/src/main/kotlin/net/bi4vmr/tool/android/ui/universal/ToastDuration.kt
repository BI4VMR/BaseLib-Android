package net.bi4vmr.tool.android.ui.universal

import android.widget.Toast

/**
 * Toast显示时长。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
enum class ToastDuration(

    /**
     * 实际传入SDK的数值。
     */
    val code: Int
) {

    /**
     * 较短时间（2秒）。
     */
    SHORT(Toast.LENGTH_SHORT),

    /**
     * 较长时间（3.5秒）。
     */
    LONG(Toast.LENGTH_LONG);
}
