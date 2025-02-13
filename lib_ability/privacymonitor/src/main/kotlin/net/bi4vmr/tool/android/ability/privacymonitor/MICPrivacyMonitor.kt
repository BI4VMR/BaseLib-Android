package net.bi4vmr.tool.android.ability.privacymonitor

import net.bi4vmr.tool.android.ability.framework.appops.AppOps

/**
 * 录音权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class MICPrivacyMonitor : PrivacyMonitor(OPS_MIC) {

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
    }
}
