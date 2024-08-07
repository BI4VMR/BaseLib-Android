package net.bi4vmr.tool.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.android.ability.privacy.CameraPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacy.LocationPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacy.MICPrivacyMonitor
import net.bi4vmr.tool.android.ability.privacy.tool.AppInfoHelper
import net.bi4vmr.tool.base.list.ListAdapterKT
import net.bi4vmr.tool.base.list.PrivacyVO
import net.bi4vmr.tool.databinding.TestuiBaseBinding

class TestUIBaseKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIBaseKT::class.java.simpleName}"
    }

    private val locationMonitor: LocationPrivacyMonitor by lazy { LocationPrivacyMonitor(applicationContext) }
    private val micMonitor: MICPrivacyMonitor by lazy { MICPrivacyMonitor(applicationContext) }
    private val cameraMonitor: CameraPrivacyMonitor by lazy { CameraPrivacyMonitor(applicationContext) }
    private val appHelper: AppInfoHelper by lazy { AppInfoHelper.getInstance(applicationContext) }

    private val binding: TestuiBaseBinding by lazy {
        TestuiBaseBinding.inflate(layoutInflater)
    }
    private val adapter: ListAdapterKT by lazy { ListAdapterKT(applicationContext) }
    private val floatWindow: PrivacyListWindowKT by lazy { PrivacyListWindowKT(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            list.adapter = adapter

            btnLocation.setOnClickListener { testLocation() }
            btnMIC.setOnClickListener { testMic() }
            btnCamera.setOnClickListener { testCamera() }
        }
    }

    // 开始监听位置权限
    private fun testLocation() {
        Log.i(TAG, "--- 开始监听位置权限 ---")
        binding.tvLog.append("\n--- 开始监听位置权限 ---\n")

        // locationMonitor.needDistinctResult = true
        locationMonitor.registerAppOpsListener { list ->
            Handler(Looper.getMainLooper()).post {
                Log.i(TAG, "$list")
                binding.tvLog.append("$list")
            }

            val datas: MutableList<PrivacyVO> = mutableListOf()
            list.forEach {
                val vo = PrivacyVO(
                    appHelper.getLabel(it.packageName) ?: "-",
                    appHelper.getIcon(it.packageName),
                    it
                )
                datas.add(vo)
            }

            floatWindow.updateData(datas)
        }
        locationMonitor.startWatchOps()
        locationMonitor.getAppOps()
        floatWindow.show()
    }

    // 开始监听录音权限
    private fun testMic() {
        Log.i(TAG, "--- 开始监听录音权限 ---")
        binding.tvLog.append("\n--- 开始监听录音权限 ---\n")

        // micMonitor.needDistinctResult = true
        micMonitor.registerAppOpsListener { list ->
            Handler(Looper.getMainLooper()).post {
                Log.i(TAG, "$list")
                binding.tvLog.append("$list")
            }

            val datas: MutableList<PrivacyVO> = mutableListOf()
            list.forEach {
                val vo = PrivacyVO(
                    appHelper.getLabel(it.packageName) ?: "-",
                    appHelper.getIcon(it.packageName),
                    it
                )
                datas.add(vo)
            }

            Handler(Looper.getMainLooper()).post {
                adapter.updateData(datas)
            }
        }
        micMonitor.startWatchOps()
    }

    // 开始监听录像权限
    private fun testCamera() {
        Log.i(TAG, "--- 开始监听录像权限 ---")
        binding.tvLog.append("\n--- 开始监听录像权限 ---\n")

        // cameraMonitor.needDistinctResult = true
        cameraMonitor.registerAppOpsListener { list ->
            Handler(Looper.getMainLooper()).post {
                Log.i(TAG, "$list")
                binding.tvLog.append("$list")
            }

            val datas: MutableList<PrivacyVO> = mutableListOf()
            list.forEach {
                val vo = PrivacyVO(
                    appHelper.getLabel(it.packageName) ?: "-",
                    appHelper.getIcon(it.packageName),
                    it
                )
                datas.add(vo)
            }

            Handler(Looper.getMainLooper()).post {
                adapter.updateData(datas)
            }
        }
        cameraMonitor.startWatchOps()
    }
}
