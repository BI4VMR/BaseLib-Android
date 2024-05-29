package net.bi4vmr.tool;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.databinding.ActivityMainBinding;
import net.bi4vmr.tool.logcat.TestUILogcat;
import net.bi4vmr.tool.logcat.TestUILogcatKT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Logcat工具
        binding.btnLogcat.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestUILogcat.class);
            startActivity(intent);
        });

        // Logcat工具(KT)
        binding.btnLogcatKT.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestUILogcatKT.class);
            startActivity(intent);
        });
    }
}
