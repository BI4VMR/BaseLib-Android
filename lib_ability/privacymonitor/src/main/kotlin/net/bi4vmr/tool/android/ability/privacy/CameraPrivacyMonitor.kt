package net.bi4vmr.tool.android.ability.privacy

import android.content.Context
import net.bi4vmr.tool.android.ability.privacy.appops.AppOps
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsMonitor

/**
 * 位置权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
class CameraPrivacyMonitor(mContext: Context) : AppOpsMonitor(mContext, OPS_CAMERA) {

    companion object {
        // 录像权限
        val OPS_CAMERA: IntArray = intArrayOf(
            AppOps.CAMERA.code,
            AppOps.PHONE_CALL_CAMERA.code
        )
    }
}
