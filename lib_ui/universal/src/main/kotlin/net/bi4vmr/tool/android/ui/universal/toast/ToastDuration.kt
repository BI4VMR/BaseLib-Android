package net.bi4vmr.tool.android.ui.universal.toast

import android.widget.Toast

/**
 * Toast显示时长。
 *
 * 目前Android不支持随意定义时长，因此通过该枚举规范时长参数的传值。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
enum class ToastDuration(

    /**
     * 实际传递给Android SDK的数值。
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
