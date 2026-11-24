package net.bi4vmr.tool.android.ability.framework.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.UserHandle
import net.bi4vmr.tool.kotlin.common.reflect.callFor
import net.bi4vmr.tool.kotlin.common.reflect.requireMethod

/**
 * [Context] 功能扩展。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ContextExtend {

    private const val TAG: String = "ContextExtend"

    private val appContext: Context = ApplicationExtend.getAppContext()


    /**
     * 注册广播接收器（指定用户）。
     *
     * @param[receiver] 广播接收器。
     * @param[filter] IntentFilter。
     * @param[userHandle] UserHandle。
     * @param[broadcastPermission] 广播权限。默认为空值。
     * @param[handler] Handler。默认为空值。
     * @param[flags] 标志。默认为 `0`。
     */
    @JvmStatic
    @JvmOverloads
    fun registerReceiverAsUser(
        receiver: BroadcastReceiver,
        filter: IntentFilter,
        userHandle: UserHandle,
        broadcastPermission: String? = null,
        handler: Handler? = null,
        flags: Int = 0
    ): Intent? =
        appContext
            .requireMethod(
                "registerReceiverAsUser",
                BroadcastReceiver::class.java,
                UserHandle::class.java,
                IntentFilter::class.java,
                String::class.java,
                Handler::class.java,
                Int::class.java
            )
            .callFor(
                appContext,
                receiver, userHandle, filter, broadcastPermission, handler, flags
            )
}
