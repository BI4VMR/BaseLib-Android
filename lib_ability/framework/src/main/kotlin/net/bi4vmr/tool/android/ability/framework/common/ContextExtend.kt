package net.bi4vmr.tool.android.ability.framework.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.UserHandle
import java.lang.reflect.Method

/**
 * [Context]的功能扩展类。
 *
 * @author bi4vmr@outlook.com
 * @since 1.0.0
 */
object ContextExtend {

    private const val TAG: String = "BaseLib-ContextExtend"

    private val appContext: Context = ApplicationExtend.getAppContext()
    private val contextClass: Class<Context> = Context::class.java

    @JvmStatic
    @JvmOverloads
    fun registerReceiverAsUser(
        receiver: BroadcastReceiver,
        filter: IntentFilter,
        userHandle: UserHandle,
        broadcastPermission: String? = null,
        handler: Handler? = null,
        flags: Int = 0
    ): Intent? {
        val method: Method = contextClass.getDeclaredMethod(
            "registerReceiverAsUser",
            BroadcastReceiver::class.java,
            UserHandle::class.java,
            IntentFilter::class.java,
            String::class.java,
            Handler::class.java,
            Int::class.java
        )
        return method.invoke(appContext, receiver, userHandle, filter, broadcastPermission, handler, flags) as? Intent
    }
}
