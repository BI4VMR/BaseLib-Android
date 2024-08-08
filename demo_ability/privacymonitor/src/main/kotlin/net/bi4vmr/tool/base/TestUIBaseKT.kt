package net.bi4vmr.tool.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.android.ability.privacy.CameraPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacy.LocationPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacy.MICPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacy.PrivacyItem
import net.bi4vmr.tool.android.ability.privacy.PrivacyMonitor
import net.bi4vmr.tool.databinding.TestuiBaseBinding

class TestUIBaseKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIBaseKT::class.java.simpleName}"
    }

    private val locationMonitor: LocationPrivacyMonitor by lazy { LocationPrivacyMonitor(applicationContext) }
    private val locationAppListener: PrivacyAppListener by lazy { PrivacyAppListener() }
    private val micMonitor: MICPrivacyMonitor by lazy { MICPrivacyMonitor(applicationContext) }
    private val micAppListener: PrivacyAppListener by lazy { PrivacyAppListener() }
    private val cameraMonitor: CameraPrivacyMonitor by lazy { CameraPrivacyMonitor(applicationContext) }
    private val cameraAppListener: PrivacyAppListener by lazy { PrivacyAppListener() }

    private val binding: TestuiBaseBinding by lazy {
        TestuiBaseBinding.inflate(layoutInflater)
    }
    private val floatWindow: PrivacyListWindowKT by lazy { PrivacyListWindowKT(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        locationMonitor.registerPrivacyItemListener(locationAppListener)
        val initList: List<PrivacyItem> = locationMonitor.getPrivacyItems()
        Log.i(TAG, "LocationPrivacyListInit. List:$initList")
        floatWindow.updateData(locationMonitor.getPrivacyItems())
    }

    // 开始监听录音权限
    private fun testMic() {
        Log.i(TAG, "--- 开始监听录音权限 ---")
        binding.tvLog.append("\n--- 开始监听录音权限 ---\n")

        floatWindow.clearData()
        floatWindow.show()

        micMonitor.registerPrivacyItemListener(locationAppListener)
        val initList: List<PrivacyItem> = micMonitor.getPrivacyItems()
        Log.i(TAG, "MicPrivacyListInit. List:$initList")
        floatWindow.updateData(initList)
    }

    // 开始监听录像权限
    private fun testCamera() {
        Log.i(TAG, "--- 开始监听录像权限 ---")
        binding.tvLog.append("\n--- 开始监听录像权限 ---\n")

        floatWindow.clearData()
        floatWindow.show()

        cameraMonitor.registerPrivacyItemListener(cameraAppListener)
        val initList: List<PrivacyItem> = cameraMonitor.getPrivacyItems()
        Log.i(TAG, "CameraPrivacyListInit. List:$initList")
        floatWindow.updateData(cameraMonitor.getPrivacyItems())
    }

    // 重置悬浮窗
    private fun testReset() {
        Log.i(TAG, "--- 重置悬浮窗 ---")
        binding.tvLog.append("\n--- 重置悬浮窗 ---\n")

        floatWindow.dismiss()

        locationMonitor.unregisterPrivacyItemListener(locationAppListener)
        micMonitor.unregisterPrivacyItemListener(micAppListener)
        cameraMonitor.unregisterPrivacyItemListener(cameraAppListener)
    }

    /**
     * 隐私权限应用监听器实现。
     */
    private inner class PrivacyAppListener : PrivacyMonitor.PrivacyItemListener() {
        override fun onListChange(items: List<PrivacyItem>) {
            Log.i(TAG, "PrivacyItemListener-OnChange. List:$items")
            floatWindow.updateData(items)
        }
    }
}
