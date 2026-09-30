package net.bi4vmr.tool.base;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.databinding.TestuiViewBinding;

public class TestUIView extends AppCompatActivity {

    private static final String TAG = "TestApp-" + TestUIView.class.getSimpleName();

    private TestuiViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TestuiViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // binding.
    }

    // 单位转换
    private void testUnitConversion() {
        Log.i(TAG, "--- 单位转换 ---");
        binding.tvLog.append("\n--- 单位转换 ---\n");
    }
}
