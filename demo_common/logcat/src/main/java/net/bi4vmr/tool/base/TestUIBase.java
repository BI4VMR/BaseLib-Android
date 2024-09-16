package net.bi4vmr.tool.base;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.android.common.logcat.Level;
import net.bi4vmr.tool.android.common.logcat.LogConfig;
import net.bi4vmr.tool.android.common.logcat.LogConfigs;
import net.bi4vmr.tool.android.common.logcat.LogUtil;
import net.bi4vmr.tool.android.common.logcat.logger.CycleLogger;
import net.bi4vmr.tool.databinding.TestuiBaseBinding;

public class TestUIBase extends AppCompatActivity {

    private TestuiBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestuiBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvLog.append("\n--- 请查看Logcat日志输出 ---\n");

        // 设置Tag前缀
        LogConfig.setTagPrefix("TestApp");

        // 日志输出全局开关
        binding.tbtnEnable.setOnCheckedChangeListener((buttonView, isChecked) -> LogConfig.setEnable(isChecked));
        // 类名显示开关
        binding.tbtnClassInfo.setOnCheckedChangeListener((buttonView, isChecked) -> LogConfig.setShowClassInfo(isChecked));
        // 方法名称显示开关
        binding.tbtnMethodInfo.setOnCheckedChangeListener((buttonView, isChecked) -> LogConfig.setShowMethodInfo(isChecked));
        // 自动换行开关
        binding.tbtnMultiLine.setOnCheckedChangeListener((buttonView, isChecked) -> LogConfig.setMultiLinePrint(isChecked));
        // 最小日志级别
        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton item = findViewById(checkedId);
            int index = group.indexOfChild(item);
            switch (index) {
                case 0:
                    LogConfig.setMinLevel(Level.VERBOSE);
                    break;
                case 1:
                    LogConfig.setMinLevel(Level.DEBUG);
                    break;
                case 2:
                    LogConfig.setMinLevel(Level.INFO);
                    break;
                case 3:
                    LogConfig.setMinLevel(Level.WARN);
                    break;
                case 4:
                    LogConfig.setMinLevel(Level.ERROR);
                    break;
            }
        });
        // 预设配置：调试版本
        binding.btnDebug.setOnClickListener(v -> LogConfigs.debug());
        // 预设配置：发布版本
        binding.btnRelease.setOnClickListener(v -> LogConfigs.release());

        // 在普通方法中输出日志
        binding.btnInFunction.setOnClickListener(v -> testPrintInFunction());
        // 在Lambda表达式中输出日志
        binding.btnInLambda.setOnClickListener(v -> {
            String message = binding.etMessage.getText().toString();
            LogUtil.d(message);
        });
        // 在内部类中输出日志
        binding.btnInInnerClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = binding.etMessage.getText().toString();
                LogUtil.d(message);
            }
        });

        // 输出超长内容
        binding.btnLongMessage.setOnClickListener(v -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= 1000; i++) {
                sb.append("日志内容");
            }
            String text = sb.toString();
            LogUtil.d(text);
        });

        // 查询循环输出任务
        binding.btnGetTask.setOnClickListener(v -> {
            String tasks = CycleLogger.getTasks().toString();
            binding.tvLog.append("循环任务列表：\n");
            binding.tvLog.append(tasks + "\n");
            LogUtil.d("循环任务列表：" + tasks);
        });

        // 新增循环输出任务
        binding.btnAddTask.setOnClickListener(v -> {
            // 新增定时任务，输出固定的文本。
            CycleLogger.addTask("Task1", 2000L, Level.INFO, "Task1", "这是一条定时日志消息。");

            // 新增定时任务，输出代码块返回的文本。
            CycleLogger.addTask("Task2", 2000L, Level.DEBUG, "Task2", continuation -> "这是一条定时日志消息，开机时长：" + SystemClock.elapsedRealtime());
        });

        // 移除循环输出任务
        binding.btnRemoveTask.setOnClickListener(v -> {
            CycleLogger.removeTask("Task1");
            CycleLogger.removeTask("Task2");
        });
    }

    // 在普通方法中输出日志
    private void testPrintInFunction() {
        String message = binding.etMessage.getText().toString();
        LogUtil.d(message);
    }
}
