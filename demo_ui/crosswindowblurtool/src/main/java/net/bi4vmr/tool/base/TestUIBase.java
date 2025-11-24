package net.bi4vmr.tool.base;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.databinding.TestuiBaseBinding;

public class TestUIBase extends AppCompatActivity {

    private static final String TAG = "BaseLib-" + TestUIBase.class.getSimpleName();

    private TestuiBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestuiBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnOpen.setOnClickListener(v -> test());
    }

    // 功能模块
    private void test() {
        Log.i(TAG, "--- 功能模块 ---");
        binding.tvLog.append("\n--- 功能模块 ---\n");

        // ...
    }
}
