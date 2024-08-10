package net.bi4vmr.tool.android.ability.privacy.tool

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log

/**
 * 应用程序信息查询工具。
 *
 * @author bi4vmr@outlook.com
 */
class AppInfoHelper private constructor(mContext: Context) {

    companion object {
        // 本实例的生命周期跟随整个进程，不会导致内存泄露，因此可以忽略警告。
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: AppInfoHelper? = null

        @JvmStatic
        fun getInstance(context: Context): AppInfoHelper {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = AppInfoHelper(context.applicationContext)
                    }
                }
            }
            return instance!!
        }

        private val TAG = "BaseLib-${AppInfoHelper::class.java.simpleName}"
    }

    private val packageManager: PackageManager = mContext.packageManager

    /**
     * 获取应用标签。
     *
     * @param[packageName] 目标应用程序包名。
     * @return 标签。若查询失败，则返回空值。
     */
    fun getLabel(packageName: String): String? {
        val cSeq: CharSequence? = getApplicationInfo(packageName)?.loadLabel(packageManager)
        return cSeq?.toString()
    }

    /**
     * 获取应用图标。
     *
     * @param[packageName] 目标应用程序包名。
     * @return 图标。若查询失败，则返回空值。
     */
    fun getIcon(packageName: String): Drawable? {
        val drawable: Drawable? = getApplicationInfo(packageName)?.loadIcon(packageManager)
        return drawable
    }

    /**
     * TODO 添加简述。
     *
     * TODO 添加详情。
     *
     * @param[TODO] 添加参数说明。
     * @return TODO 添加返回值说明。
     */
    fun isSystemApp(packageName: String): Boolean {
        val flags: Int = getApplicationInfo(packageName)?.flags ?: 0
        return (flags and ApplicationInfo.FLAG_SYSTEM) == 0
    }

    /**
     * 获取应用信息。
     *
     * @param[packageName] 待查询的应用程序包名。
     * @return ApplicationInfo对象。若包名对应的应用不存在则返回空值。
     */
    private fun getApplicationInfo(packageName: String): ApplicationInfo? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0L))
            } else {
                packageManager.getApplicationInfo(packageName, 0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Get application info failed! Reason:[${e.message}]")
            e.printStackTrace()
            null
        }
    }
}
