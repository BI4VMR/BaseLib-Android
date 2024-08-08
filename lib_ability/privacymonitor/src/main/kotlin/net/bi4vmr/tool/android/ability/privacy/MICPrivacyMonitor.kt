package net.bi4vmr.tool.android.ability.privacy

import android.content.Context
import net.bi4vmr.tool.android.ability.privacy.appops.AppOps
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsFilterCallback

/**
 * 录音权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class MICPrivacyMonitor(mContext: Context) : PrivacyMonitor(mContext, OPS_MIC) {

    companion object {

        /**
         * 录音权限列表。
         */
        val OPS_MIC: IntArray = intArrayOf(
            AppOps.RECORD_AUDIO.code,
            AppOps.PHONE_CALL_MICROPHONE.code,
            AppOps.RECEIVE_AMBIENT_TRIGGER_AUDIO.code,
            AppOps.RECEIVE_SANDBOX_TRIGGER_AUDIO.code,
            AppOps.RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO.code
        )
    }

    init {
        setAppOpsFilter(Filter())
    }

    /**
     * OP筛选器。
     */
    private inner class Filter : AppOpsFilterCallback {

        override fun test(item: OpEntity): Boolean {
            val isInCalling: Boolean = (item.opCode == AppOps.PHONE_CALL_MICROPHONE.code) && item.isRunning
            val isInRecording: Boolean = (item.opCode == AppOps.RECORD_AUDIO.code) && item.isRunning
            return isInCalling || isInRecording
        }
    }
}
