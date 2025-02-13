package net.bi4vmr.tool.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.android.ability.privacymonitor.CameraPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacymonitor.LocationPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacymonitor.MICPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacymonitor.PrivacyEventListener
import net.bi4vmr.tool.android.ability.privacymonitor.PrivacyItem
import net.bi4vmr.tool.android.ability.privacymonitor.util.PrivacyLog
import net.bi4vmr.tool.databinding.TestuiBaseBinding

class TestUIBaseKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIBaseKT::class.java.simpleName}"
    }

    private val locationMonitor: LocationPrivacyMonitor by lazy { LocationPrivacyMonitor() }
    private val locationAppListener: PrivacyAppListener by lazy { PrivacyAppListener() }
    private val micMonitor: MICPrivacyMonitor by lazy { MICPrivacyMonitor() }
    private val micAppListener: PrivacyAppListener by lazy { PrivacyAppListener() }
    private val cameraMonitor: CameraPrivacyMonitor by lazy { CameraPrivacyMonitor() }
    private val cameraAppListener: PrivacyAppListener by lazy { PrivacyAppListener() }

    private val binding: TestuiBaseBinding by lazy {
        TestuiBaseBinding.inflate(layoutInflater)
    }
    private val floatWindow: PrivacyListWindowKT by lazy { PrivacyListWindowKT(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        PrivacyLog.debug = true

        with(binding) {
            btnLocation.setOnClickListener { testLocation() }
            btnMIC.setOnClickListener { testMic() }
            btnCamera.setOnClickListener { testCamera() }
            btnReset.setOnClickListener { testReset() }
        }
    }

    // 开始监听位置权限
    private fun testLocation() {
        Log.i(TAG, "--- 开始监听位置权限 ---")
        binding.tvLog.append("\n--- 开始监听位置权限 ---\n")

        floatWindow.clearData()
        floatWindow.show()

        // locationMonitor.setIgnoreSystemApp(true)
        // locationMonitor.isDistinctByPackageName(true)
        locationMonitor.registerPrivacyEventListener(locationAppListener)
        val initList: List<PrivacyItem> = locationMonitor.getPrivacyItems()
        Log.i(TAG, "LocationPrivacyListInit. List:$initList")
        floatWindow.updateData(initList)

        val state: Boolean = locationMonitor.isInUsing()
        Log.i(TAG, "LocationPrivacyStateInit. State:[$state]")
        floatWindow.updateState(state)
    }

    // 开始监听录音权限
    private fun testMic() {
        Log.i(TAG, "--- 开始监听录音权限 ---")
        binding.tvLog.append("\n--- 开始监听录音权限 ---\n")

        floatWindow.clearData()
        floatWindow.show()

        // micMonitor.setIgnoreSystemApp(true)
        micMonitor.registerPrivacyEventListener(micAppListener)
        val initList: List<PrivacyItem> = micMonitor.getPrivacyItems()
        Log.i(TAG, "MicPrivacyListInit. List:$initList")
        floatWindow.updateData(initList)

        val state: Boolean = micMonitor.isInUsing()
        Log.i(TAG, "MicPrivacyStateInit. State:[$state]")
        floatWindow.updateState(state)
    }

    // 开始监听录像权限
    private fun testCamera() {
        Log.i(TAG, "--- 开始监听录像权限 ---")
        binding.tvLog.append("\n--- 开始监听录像权限 ---\n")

        floatWindow.clearData()
        floatWindow.show()

        // cameraMonitor.setIgnoreSystemApp(true)
        cameraMonitor.registerPrivacyEventListener(cameraAppListener)
        val initList: List<PrivacyItem> = cameraMonitor.getPrivacyItems()
        Log.i(TAG, "CameraPrivacyListInit. List:$initList")
        floatWindow.updateData(cameraMonitor.getPrivacyItems())

        val state: Boolean = cameraMonitor.isInUsing()
        Log.i(TAG, "CameraPrivacyStateInit. State:[$state]")
        floatWindow.updateState(state)
    }

    // 重置悬浮窗
    private fun testReset() {
        Log.i(TAG, "--- 重置悬浮窗 ---")
        binding.tvLog.append("\n--- 重置悬浮窗 ---\n")

        floatWindow.dismiss()

        locationMonitor.unregisterPrivacyEventListener(locationAppListener)
        micMonitor.unregisterPrivacyEventListener(micAppListener)
        cameraMonitor.unregisterPrivacyEventListener(cameraAppListener)
    }

    /**
     * 隐私权限应用监听器实现。
     */
    private inner class PrivacyAppListener : PrivacyEventListener {

        override fun onListChange(items: List<PrivacyItem>) {
            Log.i(TAG, "PrivacyAppListener-OnListChange. List:$items")
            floatWindow.updateData(items)
        }

        override fun onStateChange(state: Boolean) {
            Log.i(TAG, "PrivacyAppListener-OnStateChange. State:[$state]")
            floatWindow.updateState(state)
        }
    }
}
