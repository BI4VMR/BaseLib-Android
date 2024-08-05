package net.bi4vmr.tool.android.ability.privacy

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.bi4vmr.tool.android.ability.privacy.appops.AppOpsManagerExt
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity
import java.util.concurrent.CopyOnWriteArraySet

/**
 * 隐私权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
class PrivacyMonitor private constructor(mContext: Context) {

    companion object {
        // 本实例的生命周期跟随整个进程，不会导致内存泄露，因此可以忽略警告。
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: PrivacyMonitor? = null

        @JvmStatic
        fun getInstance(context: Context): PrivacyMonitor {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = PrivacyMonitor(context.applicationContext)
                    }
                }
            }
            return instance!!
        }

        private val TAG = "TestApp-${PrivacyMonitor::class.java.simpleName}"
    }

    private val opManagerExt: AppOpsManagerExt = AppOpsManagerExt.getInstance(mContext)
    private var job: Job? = null
    private val listeners: MutableSet<AppOpsListener> = CopyOnWriteArraySet()

    fun startWatchOps(interval: Long = 2L, ops: IntArray) {
        job = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                val opList: List<OpEntity> = opManagerExt.getPackagesOps(ops)

                delay(interval)
            }
        }
    }

    fun stopWatchOps() {
        job?.cancel()
    }

    fun registerAppOpsListener(l: AppOpsListener) {
        listeners.add(l)
    }

    fun unregisterAppOpsListener(l: AppOpsListener) {
        listeners.remove(l)
    }

    private fun notifyAppOpsChange(ops: List<OpEntity>) {
        listeners.forEach {
            it.onChange(ops)
        }
    }
}
