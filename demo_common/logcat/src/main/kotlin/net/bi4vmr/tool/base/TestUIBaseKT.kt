package net.bi4vmr.tool.base

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.view.View.OnClickListener
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import net.bi4vmr.tool.android.common.logcat.Level
import net.bi4vmr.tool.android.common.logcat.LogConfig
import net.bi4vmr.tool.android.common.logcat.LogConfig.tagPrefix
import net.bi4vmr.tool.android.common.logcat.LogConfigs
import net.bi4vmr.tool.android.common.logcat.LogUtil
import net.bi4vmr.tool.android.common.logcat.logger.CycleLogger
import net.bi4vmr.tool.android.common.logcat.logger.CycleLogger.removeTask
import net.bi4vmr.tool.databinding.TestuiBaseBinding

class TestUIBaseKT : AppCompatActivity() {

    private val binding: TestuiBaseBinding by lazy {
        TestuiBaseBinding.inflate(layoutInflater)
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

            // 查询循环输出任务
            btnGetTask.setOnClickListener {
                val tasks: String = CycleLogger.getTasks().toString()
                binding.tvLog.append("循环任务列表：\n")
                binding.tvLog.append("$tasks\n")
                LogUtil.d("循环任务列表：$tasks")
            }

            // 新增循环输出任务
            btnAddTask.setOnClickListener {
                // 新增定时任务，输出固定的文本。
                CycleLogger.addTask("Task1", 2000L, Level.INFO, "Task1", "这是一条定时日志消息。")

                // 新增定时任务，输出代码块返回的文本。
                CycleLogger.addTask("Task2", 2000L, Level.INFO, "Task2") {
                    "这是一条定时日志消息，开机时长：${SystemClock.elapsedRealtime()}"
                }
            }

            // 移除循环输出任务
            btnRemoveTask.setOnClickListener {
                removeTask("Task1")
                removeTask("Task2")
            }
        }
    }

    // 在普通方法中输出日志
    private fun testPrintInFunction() {
        val message: String = binding.etMessage.text.toString()
        LogUtil.d(message)
    }
}
