package net.bi4vmr.tool.android.ability.privacy

import android.content.Context
import net.bi4vmr.tool.android.ability.privacy.appops.AppOps
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsMonitor

/**
 * 位置权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
class MICPrivacyMonitor(mContext: Context) : AppOpsMonitor(mContext, OPS_MIC) {

    companion object {
        // 录音权限
        val OPS_MIC: IntArray = intArrayOf(
            AppOps.RECORD_AUDIO.code,
            AppOps.PHONE_CALL_MICROPHONE.code,
            AppOps.RECEIVE_AMBIENT_TRIGGER_AUDIO.code,
            AppOps.RECEIVE_SANDBOX_TRIGGER_AUDIO.code,
            AppOps.RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO.code
        )
    }
}
