package net.bi4vmr.tool.android.ability.privacy

import android.annotation.SuppressLint
import android.content.Context
import net.bi4vmr.tool.android.ability.privacy.appops.AppOpsManagerExt

/**
 * TODO
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
}
