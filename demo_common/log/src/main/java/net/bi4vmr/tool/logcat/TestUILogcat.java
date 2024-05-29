package net.bi4vmr.tool.logcat;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.android.common.log.logcat.Level;
import net.bi4vmr.tool.android.common.log.logcat.LogConfig;
import net.bi4vmr.tool.android.common.log.logcat.LogConfigs;
import net.bi4vmr.tool.android.common.log.logcat.LogUtil;
import net.bi4vmr.tool.databinding.TestuiLogcatBinding;

public class TestUILogcat extends AppCompatActivity {

    private TestuiLogcatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestuiLogcatBinding.inflate(getLayoutInflater());
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
    }

    // 在普通方法中输出日志
    private void testPrintInFunction() {
        String message = binding.etMessage.getText().toString();
        LogUtil.d(message);
    }
}
