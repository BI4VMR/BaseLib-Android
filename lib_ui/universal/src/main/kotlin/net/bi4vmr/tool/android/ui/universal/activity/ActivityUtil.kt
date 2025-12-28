package net.bi4vmr.tool.android.ui.universal.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * Activity相关工具。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ActivityUtil {

    /**
     * 打开Activity（指明目标类）。
     *
     * @param[context] 上下文。
     * @param[clazz] 目标Activity的Class。
     * @param[intent] 额外数据与标志位等。可选，默认为空。
     * @param[options] 选项。可选，默认为空。
     */
    @JvmOverloads
    fun openActivity(context: Context, clazz: Class<*>, intent: Intent? = null, options: Bundle? = null) {
        val newIntent = if (intent == null) {
            Intent()
        } else {
            // 如果调用者制定了Intent，复制调用者指定的Intent配置。
            Intent(intent)
        }
        // 指明目标类
        newIntent.setClass(context, clazz)

        context.startActivity(newIntent, options)
    }

    /**
     * 打开Activity（指明目标类）。
     *
     * 函数式接口，通过Lambda配置Intent和Options。
     *
     * @param[context] 上下文。
     * @param[clazz] 目标Activity的Class。
     * @param[configIntent] Lambda表达式， `this` 为 [Intent] ，用于配置额外数据与标志位等。可选，默认不做任何配置。
     * @param[configOptions] Lambda表达式， `this` 为 [Bundle] ，用于配置选项。可选，默认不做任何配置。
     */
    @JvmOverloads
    fun openActivityKT(
        context: Context,
        clazz: Class<*>,
        configIntent: Intent.() -> Unit = {},
        configOptions: Bundle.() -> Unit = {}
    ) {
        val intent = Intent()
        // 调用Lambda配置Intent
        configIntent.invoke(intent)
        intent.setClass(context, clazz)

        val options = Bundle()
        // 调用Lambda配置Options
        configOptions.invoke(options)

        openActivity(context, clazz, intent, options)
    }
}
