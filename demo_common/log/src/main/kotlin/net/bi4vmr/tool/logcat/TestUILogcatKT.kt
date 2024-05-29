package net.bi4vmr.tool.logcat

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.android.common.log.logcat.Level
import net.bi4vmr.tool.android.common.log.logcat.LogConfig
import net.bi4vmr.tool.android.common.log.logcat.LogConfig.tagPrefix
import net.bi4vmr.tool.android.common.log.logcat.LogConfigs
import net.bi4vmr.tool.android.common.log.logcat.LogUtil
import net.bi4vmr.tool.databinding.TestuiLogcatBinding

class TestUILogcatKT : AppCompatActivity() {

    private val binding: TestuiLogcatBinding by lazy {
        TestuiLogcatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvLog.append("\n--- 请查看Logcat日志输出 ---\n")

        // 设置Tag前缀
        tagPrefix = "TestApp"

        with(binding) {
            // 日志输出全局开关
            tbtnEnable.setOnCheckedChangeListener { _, isChecked -> LogConfig.enable = isChecked }
            // 类名显示开关
            tbtnClassInfo.setOnCheckedChangeListener { _, isChecked -> LogConfig.showClassInfo = isChecked }
            // 方法名称显示开关
            tbtnMethodInfo.setOnCheckedChangeListener { _, isChecked -> LogConfig.showMethodInfo = isChecked }
            // 自动换行开关
            tbtnMultiLine.setOnCheckedChangeListener { _, isChecked -> LogConfig.multiLinePrint = isChecked }
            // 最小日志级别
            binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
                val item: RadioButton = findViewById(checkedId)
                val index: Int = group.indexOfChild(item)
                when (index) {
                    0 -> LogConfig.minLevel = Level.VERBOSE
                    1 -> LogConfig.minLevel = Level.DEBUG
                    2 -> LogConfig.minLevel = Level.INFO
                    3 -> LogConfig.minLevel = Level.WARN
                    4 -> LogConfig.minLevel = Level.ERROR
                }
            }
            // 预设配置：调试版本
            binding.btnDebug.setOnClickListener { LogConfigs.debug() }
            // 预设配置：发布版本
            binding.btnRelease.setOnClickListener { LogConfigs.release() }

            // 在普通方法中输出日志
            btnInFunction.setOnClickListener { testPrintInFunction() }
            // 在Lambda表达式中输出日志
            btnInLambda.setOnClickListener {
                val message: String = binding.etMessage.text.toString()
                LogUtil.d(message)
            }
            // 在内部类中输出日志
            btnInInnerClass.setOnClickListener(object : OnClickListener {
                override fun onClick(v: View) {
                    val message: String = binding.etMessage.text.toString()
                    LogUtil.d(message)
                }
            })

            // 输出超长内容
            btnLongMessage.setOnClickListener {
                // 测试数据
                val stringBuilder = StringBuilder()
                for (i in 1..1000) {
                    stringBuilder.append("日志内容")
                }
                val text: String = stringBuilder.toString()
                LogUtil.d(text)
            }
        }
    }

    // 在普通方法中输出日志
    private fun testPrintInFunction() {
        val message: String = binding.etMessage.text.toString()
        LogUtil.d(message)
    }
}
