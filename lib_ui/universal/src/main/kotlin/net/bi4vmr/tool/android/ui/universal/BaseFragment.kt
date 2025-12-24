package net.bi4vmr.tool.android.ui.universal

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
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
     * 打开Activity。
     *
     * @param[clazz] 目标Activity的Class。
     * @param[extras] 额外数据。可选，默认为空。
     * @param[options] 选项。可选，默认为空。
     */
    @JvmOverloads
    fun openActivity(clazz: Class<*>, extras: Bundle? = null, options: Bundle? = null) {
        val intent = Intent()
        intent.setClass(requireActivity(), clazz)
        extras?.let {
            intent.putExtras(it)
        }
        startActivity(intent, options)
    }

    /**
     * 打开Activity。
     *
     * @param[component] 目标Activity的ComponentName。
     * @param[extras] 额外数据。可选，默认为空。
     * @param[options] 选项。可选，默认为空。
     */
    @JvmOverloads
    fun openActivity(component: ComponentName, extras: Bundle? = null, options: Bundle? = null) {
        val intent = Intent()
        intent.setComponent(component)
        extras?.let {
            intent.putExtras(it)
        }
        startActivity(intent, options)
    }

    /**
     * 打开Activity。
     *
     * @param[pkgName] 目标Activity的包名。
     * @param[clsName] 目标Activity的类名。
     * @param[extras] 额外数据。可选，默认为空。
     * @param[options] 选项。可选，默认为空。
     */
    @JvmOverloads
    fun openActivity(pkgName: String, clsName: String, extras: Bundle? = null, options: Bundle? = null) {
        val cmp = ComponentName(pkgName, clsName)
        openActivity(cmp, extras, options)
    }

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
    fun showToast(text: Any, duration: ToastDuration = ToastDuration.SHORT) {
        val message = text as? CharSequence ?: text.toString()

        activity?.runOnUiThread {
            Toast.makeText(requireContext(), message, duration.code)
                .show()
        }
    }
}
