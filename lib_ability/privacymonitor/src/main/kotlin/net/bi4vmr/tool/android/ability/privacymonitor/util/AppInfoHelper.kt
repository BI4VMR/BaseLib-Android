package net.bi4vmr.tool.android.ability.privacymonitor.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import net.bi4vmr.tool.android.ability.framework.common.ApplicationExtend

/**
 * 应用程序信息查询工具。
 *
 * @author bi4vmr@outlook.com
 */
object AppInfoHelper {

    private const val TAG: String = "BaseLib-AppInfoHelper"

    private val appContext: Context = ApplicationExtend.getAppContext()
    private val packageManager: PackageManager = appContext.packageManager

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
     * 判断应用是否为系统应用。
     *
     * @param[packageName] 待查询的应用程序包名。
     * @return "true"表示是系统应用，"false"表示不是系统应用。
     */
    fun isSystemApp(packageName: String): Boolean {
        val flags: Int = getApplicationInfo(packageName)?.flags ?: 0
        return (flags and ApplicationInfo.FLAG_SYSTEM) != 0
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
