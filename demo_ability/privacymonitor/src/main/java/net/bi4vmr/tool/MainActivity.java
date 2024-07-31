package net.bi4vmr.tool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.base.TestUIBaseKT;
import net.bi4vmr.tool.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        // // Logcat工具
        // binding.btnLogcat.setOnClickListener(v -> {
        //     Intent intent = new Intent(this, TestUILogcat.class);
        //     startActivity(intent);
        // });

        // Logcat工具(KT)
        binding.btnLogcatKT.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestUIBaseKT.class);
            startActivity(intent);
        });
    }
}
