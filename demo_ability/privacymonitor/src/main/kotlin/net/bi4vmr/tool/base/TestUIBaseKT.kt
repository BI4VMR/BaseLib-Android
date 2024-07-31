package net.bi4vmr.tool.base

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.android.ability.privacy.appops.AppOpsManagerExt
import net.bi4vmr.tool.android.ability.privacy.tool.AppInfoHelper
import net.bi4vmr.tool.base.list.ListAdapterKT
import net.bi4vmr.tool.base.list.PrivacyVO
import net.bi4vmr.tool.databinding.TestuiBaseBinding

class TestUIBaseKT : AppCompatActivity() {

    companion object {
        private val TAG: String = "TestApp-${TestUIBaseKT::class.java.simpleName}"
    }

    private val appOpsManager: AppOpsManagerExt by lazy { AppOpsManagerExt.getInstance(applicationContext) }
    private val appHelper: AppInfoHelper by lazy { AppInfoHelper.getInstance(applicationContext) }

    private val binding: TestuiBaseBinding by lazy {
        TestuiBaseBinding.inflate(layoutInflater)
    }
    private val adapter: ListAdapterKT by lazy { ListAdapterKT(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            list.adapter = adapter

            btnGet.setOnClickListener { test() }
        }
    }

    // 功能模块
    private fun test() {
        Log.i(TAG, "--- 功能模块 ---")
        binding.tvLog.append("\n--- 功能模块 ---\n")

        val datas: MutableList<PrivacyVO> = mutableListOf()
        appOpsManager.getPackagesOps(null).forEach {
            Log.i(TAG, "$it")
            binding.tvLog.append("$it")
            val vo = PrivacyVO(
                appHelper.getLabel(it.packageName) ?: "-",
                appHelper.getIcon(it.packageName),
                it
            )
            datas.add(vo)
        }
        adapter.updateData(datas)
    }
}
