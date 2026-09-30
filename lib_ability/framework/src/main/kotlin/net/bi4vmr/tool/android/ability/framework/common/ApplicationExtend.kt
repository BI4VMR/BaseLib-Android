package net.bi4vmr.tool.android.ability.framework.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.reflect.Method

/**
 * [Application]的功能扩展类。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
object ApplicationExtend {

    private const val TAG: String = "BaseLib-ApplicationExtend"

    @JvmStatic
    fun getApplication(): Application {
        val activityThreadClass: Class<*> = Class.forName("android.app.ActivityThread")
        val method: Method = activityThreadClass.getDeclaredMethod("currentApplication")
        method.isAccessible = true
        return method.invoke(null) as Application
    }

    @JvmStatic
    fun getAppContext(): Context {
        return getApplication().applicationContext
    }
}
