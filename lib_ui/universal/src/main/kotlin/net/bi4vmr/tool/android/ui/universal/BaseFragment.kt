package net.bi4vmr.tool.android.ui.universal

import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * 基础Fragment。
 *
 * 提供一些常用的方法。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
abstract class BaseFragment : Fragment() {

    /**
     * 关闭宿主Activity。
     */
    fun finishActivity() {
        activity?.finish()
    }

    /**
     * 显示Toast消息。
     *
     * @param[text] 文本内容。
     * @param[duration] 显示时长，默认为 [Toast.LENGTH_SHORT] 。
     */
    @JvmOverloads
    fun showToast(text: Any, duration: Int = Toast.LENGTH_SHORT) {
        val message = text as? CharSequence ?: text.toString()

        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, duration)
                .show()
        }
    }
}
