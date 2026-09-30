package net.bi4vmr.tool.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.databinding.TestuiBaseBinding

class TestUIBaseKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIBaseKT::class.java.simpleName}"
    }

    private val binding: TestuiBaseBinding by lazy {
        TestuiBaseBinding.inflate(layoutInflater)
    }
    private val floatWindow: TestWindowKT by lazy { TestWindowKT(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnOpen.setOnClickListener { testOpen() }
            btnReset.setOnClickListener { testReset() }
        }
    }

    private fun testOpen() {
        Log.i(TAG, "--- 打开悬浮窗 ---")
        binding.tvLog.append("\n--- 打开悬浮窗 ---\n")

        floatWindow.show()
    }

    private fun testReset() {
        Log.i(TAG, "--- 重置悬浮窗 ---")
        binding.tvLog.append("\n--- 重置悬浮窗 ---\n")

        floatWindow.dismiss()
    }
}
