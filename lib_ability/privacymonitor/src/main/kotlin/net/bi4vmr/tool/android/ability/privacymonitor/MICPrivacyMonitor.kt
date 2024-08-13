package net.bi4vmr.tool.android.ability.privacymonitor

import android.content.Context
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOps
import net.bi4vmr.tool.android.ability.privacymonitor.util.PrivacyLog

/**
 * 录音权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class MICPrivacyMonitor(mContext: Context) : PrivacyMonitor(mContext, OPS_MIC) {

    companion object {

        /**
         * 录音OP列表。
         */
        val OPS_MIC: IntArray = intArrayOf(
            // 录音
            AppOps.RECORD_AUDIO.code,
            // 语音通话
            AppOps.PHONE_CALL_MICROPHONE.code
        )

        private val TAG = "${PrivacyLog.TAG_PREFIX}${MICPrivacyMonitor::class.java.simpleName}"
    }
}
