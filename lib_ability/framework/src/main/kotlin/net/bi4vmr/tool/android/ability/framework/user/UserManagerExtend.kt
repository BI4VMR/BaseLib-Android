package net.bi4vmr.tool.android.ability.framework.user

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import android.os.UserHandle
import android.os.UserManager
import net.bi4vmr.tool.android.ability.framework.common.ApplicationExtend
import net.bi4vmr.tool.android.ability.framework.util.FrameworkLog
import java.lang.reflect.Method

/**
 * [UserManager]的功能扩展类。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object UserManagerExtend {

    /**
     * 特殊用户ID：系统用户。
     *
     * 本常量值与 [UserHandle] 中的 `USER_SYSTEM` 常量保持一致。
     */
    const val USER_ID_SYSTEM: Int = 0

    /**
     * 特殊用户ID：所有用户。
     *
     * 本常量值与 [UserHandle] 中的 `USER_ALL` 常量保持一致。
     */
    const val USER_ID_ALL: Int = -1

    /**
     * 特殊用户ID：当前用户。
     *
     * 本常量值与 [UserHandle] 中的 `USER_CURRENT` 常量保持一致。
     */
    const val USER_ID_CURRENT: Int = -2

    /**
     * 特殊用户：系统用户。
     *
     * 本变量值与 [UserHandle] 中的 `SYSTEM` 常量保持一致。
     */
    @JvmField
    val USER_HANDLE_SYSTEM: UserHandle = getUserHandleByUserID(USER_ID_SYSTEM)

    /**
     * 特殊用户：所有用户。
     *
     * 本变量值与 [UserHandle] 中的 `ALL` 常量保持一致。
     */
    @JvmField
    val USER_HANDLE_ALL: UserHandle = getUserHandleByUserID(USER_ID_ALL)

    /**
     * 特殊用户：当前用户。
     *
     * 本变量值与 [UserHandle] 中的 `CURRENT` 常量保持一致。
     */
    @JvmField
    val USER_HANDLE_CURRENT: UserHandle = getUserHandleByUserID(USER_ID_CURRENT)


    private const val TAG: String = "BaseLib-UserManagerExtend"

    // UserID的范围，用于分离UserID和AppID。
    private const val PER_USER_RANGE: Int = 100000

    // AppID的最小值
    private const val APPID_RANGE_MIN: Int = 10000

    private val appContext: Context = ApplicationExtend.getAppContext()
    private val userManagerClass: Class<UserManager> = UserManager::class.java
    private val userManager: UserManager = appContext.getSystemService(userManagerClass)
    private val userHandleClass: Class<UserHandle> = UserHandle::class.java
    private val activityManagerClass: Class<ActivityManager> = ActivityManager::class.java
    // private val activityManager: ActivityManager = appContext.getSystemService(activityManagerClass)

    /**
     * 获取当前进程的UID。
     *
     * @return UID。
     */
    @JvmStatic
    fun getUID(): Int {
        return Process.myUid()
    }

    /**
     * 获取当前用户的UserID。
     *
     * 该方法优先使用反射调用ActivityManager的"getCurrentUser()"方法进行操作，调用者需要申请以下权限之一：
     *
     * - `android.permission.INTERACT_ACROSS_USERS`
     * - `android.permission.INTERACT_ACROSS_USERS_FULL`
     *
     * 如果反射操作失败，则使用当前进程的UID计算UserID。
     *
     * @return 用户ID。
     */
    @JvmStatic
    fun getUserID(): Int {
        return try {
            val method: Method = activityManagerClass.getMethod("getCurrentUser")
            method.invoke(null) as Int
        } catch (e: Exception) {
            FrameworkLog.logE(TAG, "Reflect operate failed! Reason:[${e.message}]", e)
            // 反射操作失败，则回退到使用UID计算UserID。
            getUserID(getUID())
        }
    }

    /**
     * 获取当前应用程序的AppID。
     *
     * @return AppID。
     */
    @JvmStatic
    fun getAppID(): Int {
        return getAppId(getUID())
    }

    /**
     * 判断当前系统是否支持多用户特性。
     *
     * @return "true"表示支持，"false"表示不支持。
     */
    @JvmStatic
    fun supportMultiUser(): Boolean {
        return UserManager.supportsMultipleUsers()
    }

    /**
     * 从UID中分离UserID。
     *
     * @param[uid] UID。
     * @return UserID。
     */
    @JvmStatic
    fun getUserID(uid: Int): Int {
        return if (supportMultiUser()) {
            uid / PER_USER_RANGE
        } else {
            USER_ID_SYSTEM
        }
    }

    /**
     * 从UID中分离AppID。
     *
     * @param[uid] UID。
     * @return AppID。
     */
    @JvmStatic
    fun getAppId(uid: Int): Int {
        return uid % PER_USER_RANGE
    }

    /**
     * 组合UserID和AppID生成UID。
     *
     * @param[userID] UserID。
     * @param[appID] AppID。
     * @return UID。
     */
    @JvmStatic
    fun getUID(userID: Int, appID: Int): Int {
        return if (supportMultiUser()) {
            (userID * PER_USER_RANGE) + (appID % PER_USER_RANGE)
        } else {
            appID
        }
    }


    /**
     * 获取当前用户的 [UserHandle] 对象。
     *
     * @return [UserHandle]。
     */
    @JvmStatic
    fun getUserHandle(): UserHandle {
        return Process.myUserHandle()
    }

    /**
     * 获取指定UID对应的 [UserHandle] 对象。
     *
     * @param[uid] UID。
     * @return [UserHandle]。
     */
    @JvmStatic
    fun getUserHandleByUID(uid: Int): UserHandle {
        return UserHandle.getUserHandleForUid(uid)
    }

    /**
     * 获取指定UserID对应的 [UserHandle] 对象。
     *
     * @param[userID] UserID。
     * @return [UserHandle]。
     */
    @JvmStatic
    fun getUserHandleByUserID(userID: Int): UserHandle {
        return try {
            val method: Method = userHandleClass.getDeclaredMethod("of", Int::class.java)
            method.invoke(null, userID) as UserHandle
        } catch (e: Exception) {
            FrameworkLog.logE(TAG, "Reflect operate failed! Reason:[${e.message}]", e)
            // 反射操作失败，则回退到使用UID构造对象。
            getUserHandleByUID(getUID(userID, APPID_RANGE_MIN))
        }
    }
}
