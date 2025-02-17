package net.bi4vmr.tool.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.R
import net.bi4vmr.tool.android.ui.universal.ResourceUtil
import net.bi4vmr.tool.databinding.TestuiViewBinding

class TestUIViewKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIViewKT::class.java.simpleName}"
    }

    private val binding: TestuiViewBinding by lazy {
        TestuiViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            btnResUtil.setOnClickListener { testResUtil() }
        }
    }

    // 单位转换
    private fun testUnitConversion() {
        Log.i(TAG, "--- 单位转换 ---")
        binding.tvLog.append("\n--- 单位转换 ---\n")
    }

    // 资源工具
    private fun testResUtil() {
        Log.i(TAG, "--- 资源工具 ---")
        binding.tvLog.append("\n--- 资源工具 ---\n")

        ResourceUtil.getColorFromAttr(this, R.attr.titleColor)
        ResourceUtil.getColorFromAttr(this, androidx.appcompat.R.attr.actionBarSize)
    }
}
