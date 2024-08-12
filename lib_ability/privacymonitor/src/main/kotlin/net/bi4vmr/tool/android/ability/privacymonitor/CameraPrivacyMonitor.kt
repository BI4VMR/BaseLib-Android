package net.bi4vmr.tool.android.ability.privacymonitor

import android.content.Context
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOps
import net.bi4vmr.tool.android.ability.privacymonitor.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacymonitor.util.PrivacyLog

/**
 * 录像权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class CameraPrivacyMonitor(mContext: Context) : PrivacyMonitor(mContext, OPS_CAMERA) {

    companion object {

        /**
         * 录像OP列表。
         */
        val OPS_CAMERA: IntArray = intArrayOf(
            // 摄像头
            AppOps.CAMERA.code,
            // 视频通话
            AppOps.PHONE_CALL_CAMERA.code
        )

        private val TAG = "${PrivacyLog.TAG_PREFIX}${CameraPrivacyMonitor::class.java.simpleName}"
    }

    init {
        setAppOpsFilter(Filter())
    }

    /**
     * OP筛选器。
     */
    private inner class Filter : AppOpsFilterCallback {

        override fun test(item: OpEntity): Boolean {
            val isInCalling: Boolean = (item.opCode == AppOps.PHONE_CALL_CAMERA.code) && item.running
            val isInRecording: Boolean = (item.opCode == AppOps.CAMERA.code) && item.running
            return isInCalling || isInRecording
        }
    }
}
