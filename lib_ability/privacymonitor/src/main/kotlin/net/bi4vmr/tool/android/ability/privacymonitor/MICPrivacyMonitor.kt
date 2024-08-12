package net.bi4vmr.tool.android.ability.privacymonitor

import android.app.AppOpsManager
import android.content.Context
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOps
import net.bi4vmr.tool.android.ability.privacymonitor.appops.OpEntity

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
            AppOps.PHONE_CALL_MICROPHONE.code
        )
    }

    init {
        setAppOpsFilter(Filter())
        AppOpsManager.OPSTR_READ_CELL_BROADCASTS
    }

    /**
     * OP筛选器。
     */
    private inner class Filter : AppOpsFilterCallback {

        override fun test(item: OpEntity): Boolean {
            val isInCalling: Boolean = (item.opCode == AppOps.PHONE_CALL_MICROPHONE.code) && item.running
            val isInRecording: Boolean = (item.opCode == AppOps.RECORD_AUDIO.code) && item.running
            return isInCalling || isInRecording
        }
    }
}
