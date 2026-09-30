package net.bi4vmr.tool.android.ui.universal.toast

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

/**
 * Toast相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ToastUtil {

    /**
     * 显示Toast消息。
     *
     * @param[context] 上下文。
     * @param[message] 消息内容。
     * @param[duration] 显示时长，默认为 [Toast.LENGTH_SHORT] 。
     */
    @JvmOverloads
    fun showToast(context: Context, message: Any, duration: ToastDuration = ToastDuration.SHORT) {
        // 如果调用者传入的不是文本，则调用 `toString()` 方法获取文本。
        val text = message as? CharSequence ?: message.toString()

        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(context, text, duration.code).show()
        } else {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, text, duration.code).show()
            }
        }
    }
}
