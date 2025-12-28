package net.bi4vmr.tool.android.ui.universal.base

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import net.bi4vmr.tool.android.ui.universal.activity.ActivityUtil
import net.bi4vmr.tool.android.ui.universal.toast.ToastDuration
import net.bi4vmr.tool.android.ui.universal.toast.ToastUtil

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
     * 打开Activity（指明目标类）。
     *
     * @param[clazz] 目标Activity的Class。
     * @param[intent] 额外数据与标志位等。可选，默认为空。
     * @param[options] 选项。可选，默认为空。
     */
    @JvmOverloads
    fun openActivity(clazz: Class<*>, intent: Intent? = null, options: Bundle? = null) {
        ActivityUtil.openActivity(requireActivity(), clazz, intent, options)
    }

    /**
     * 打开Activity（指明目标类）。
     *
     * 函数式接口，通过Lambda配置Intent和Options。
     *
     * @param[clazz] 目标Activity的Class。
     * @param[configIntent] Lambda表达式， `this` 为 [Intent] ，用于配置额外数据与标志位等。可选，默认不做任何配置。
     * @param[configOptions] Lambda表达式， `this` 为 [Bundle] ，用于配置选项。可选，默认不做任何配置。
     */
    @JvmOverloads
    fun openActivity(
        clazz: Class<*>,
        configIntent: Intent.() -> Unit = {},
        configOptions: Bundle.() -> Unit = {}
    ) {
        ActivityUtil.openActivity(requireActivity(), clazz, configIntent, configOptions)
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
     * @param[message] 消息内容。
     * @param[duration] 显示时长，默认为 [Toast.LENGTH_SHORT] 。
     */
    @JvmOverloads
    fun showToast(message: Any, duration: ToastDuration = ToastDuration.SHORT) {
        activity?.let {
            ToastUtil.showToast(it, message, duration)
        }
    }
}
