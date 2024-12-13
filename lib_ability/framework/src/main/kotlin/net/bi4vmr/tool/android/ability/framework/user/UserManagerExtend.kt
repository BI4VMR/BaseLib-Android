package net.bi4vmr.tool.android.ability.framework.user

import android.app.ActivityManager
import android.os.UserHandle
import android.os.UserManager
import java.lang.reflect.Method

/**
 * [UserManager]的功能扩展类。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object UserManagerExtend {

    /**
     * 特殊用户ID：所有用户。
     *
     * 本变量值与[UserHandle]中的 `USER_ALL` 常量保持一致。
     */
    const val USER_ID_ALL: Int = -1

    /**
     * 特殊用户ID：当前用户。
     *
     * 本变量值与[UserHandle]中的 `USER_CURRENT` 常量保持一致。
     */
    const val USER_ID_CURRENT: Int = -2

    private const val TAG: String = "BaseLib-UserManagerExtend"

    // private val appContext: Context = ApplicationExtend.getAppContext()
    // private val userManagerClass = UserManager::class.java
    // private val userManager: UserManager = appContext.getSystemService(userManagerClass)
    private val activityManagerClass: Class<ActivityManager> = ActivityManager::class.java
    // private val activityManager: ActivityManager = appContext.getSystemService(activityManagerClass)

    /**
     * TODO 添加简述。
     *
     * "android.permission.INTERACT_ACROSS_USERS"
     * "android.permission.INTERACT_ACROSS_USERS_FULL"
     *
     * @param
     * @return
     */
    @JvmStatic
    fun getCurrentUserID(): Int {
        val method: Method = activityManagerClass.getMethod("getCurrentUser")
        return method.invoke(null) as Int
    }
}
