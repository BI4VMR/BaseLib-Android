package net.bi4vmr.tool.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import net.bi4vmr.tool.android.ability.privacy.appops.AppOps
import net.bi4vmr.tool.android.ability.privacy.appops.AppOpsManagerExt

/**
 * 示例服务：隐私权限使用状况。
 *
 * ```text
 * am start-service "net.bi4vmr.study.system.ability.permission/net.bi4vmr.study.privacy.TestServicePrivacy"
 * ```
 *
 * @author bi4vmr@outlook.com
 */
class TestServiceKT : Service() {

    companion object {
        private val TAG: String = "BaseLib-${TestServiceKT::class.java.getSimpleName()}"

        // 定位权限
        val OPS_LOCATION: IntArray = intArrayOf(
            AppOps.COARSE_LOCATION.code,
            AppOps.FINE_LOCATION.code,
            AppOps.MONITOR_HIGH_POWER_LOCATION.code
        )
        // 录音权限
        val OPS_MIC: IntArray = intArrayOf(
            AppOps.RECORD_AUDIO.code,
            AppOps.PHONE_CALL_MICROPHONE.code,
            AppOps.RECEIVE_AMBIENT_TRIGGER_AUDIO.code,
            AppOps.RECEIVE_SANDBOX_TRIGGER_AUDIO.code,
            AppOps.RECEIVE_EXPLICIT_USER_INTERACTION_AUDIO.code
        )
        // 录像权限
        val OPS_CAMERA: IntArray = intArrayOf(
            AppOps.CAMERA.code,
            AppOps.PHONE_CALL_CAMERA.code
        )
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "GetPackagesOps. Type:[LOCATION]")
        AppOpsManagerExt.getInstance(applicationContext)
            // 查询OP记录
            .getPackagesOps(OPS_LOCATION)
            // 输出到日志
            .forEach {
                Log.i(TAG, "APP:[${it.packageName}] OP:[${it.opName}] Running:[${it.isRunning}]")
            }

        Log.i(TAG, "------")
        Log.i(TAG, "GetPackagesOps. Type:[MIC]")
        AppOpsManagerExt.getInstance(applicationContext)
            // 查询OP记录
            .getPackagesOps(OPS_MIC)
            // 输出到日志
            .forEach {
                Log.i(TAG, "APP:[${it.packageName}] OP:[${it.opName}] Running:[${it.isRunning}]")
            }

        Log.i(TAG, "------")
        Log.i(TAG, "GetPackagesOps. Type:[CAMERA]")
        AppOpsManagerExt.getInstance(applicationContext)
            // 查询OP记录
            .getPackagesOps(OPS_CAMERA)
            // 输出到日志
            .forEach {
                Log.i(TAG, "APP:[${it.packageName}] OP:[${it.opName}] Running:[${it.isRunning}]")
            }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
