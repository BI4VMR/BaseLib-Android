package net.bi4vmr.tool.android.ability.framework.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import java.lang.reflect.Method

/**
 * [Application] 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
@SuppressLint("PrivateApi", "DiscouragedPrivateApi")
object ApplicationExtend {

    private const val TAG: String = "ApplicationExtend"


    /**
     * 获取 [Application] 实例。
     *
     * @return [Application] 实例。
     */
    @JvmStatic
    fun getApplication(): Application {
        val activityThreadClass: Class<*> = Class.forName("android.app.ActivityThread")
        val method: Method = activityThreadClass.getDeclaredMethod("currentApplication")
        method.isAccessible = true
        return method.invoke(null) as Application
    }

    /**
     * 获取应用级 Context 实例。
     *
     * @return Context 实例。
     */
    @JvmStatic
    fun getAppContext(): Context {
        return getApplication().applicationContext
    }
}
