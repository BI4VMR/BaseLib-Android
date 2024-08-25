package net.bi4vmr.tool;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.bi4vmr.tool.databinding.ActivityMainBinding;
import net.bi4vmr.tool.base.TestUIView;
import net.bi4vmr.tool.base.TestUIViewKT;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // View工具
        binding.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestUIView.class);
            startActivity(intent);
        });

        // View工具(KT)
        binding.btnViewKT.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestUIViewKT.class);
            startActivity(intent);
        });
    }
}
