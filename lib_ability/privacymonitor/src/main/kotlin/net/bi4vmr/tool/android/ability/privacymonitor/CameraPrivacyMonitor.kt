package net.bi4vmr.tool.android.ability.privacymonitor

import net.bi4vmr.tool.android.ability.framework.appops.AppOps

/**
 * 录像权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class CameraPrivacyMonitor : PrivacyMonitor(OPS_CAMERA) {

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
    }
}
