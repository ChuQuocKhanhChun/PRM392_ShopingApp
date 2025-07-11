package com.manager.glassshoping.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.manager.glassshoping.R;

import soup.neumorphism.NeumorphCardView;

public class QuanLiActivity extends AppCompatActivity {
    NeumorphCardView themsp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_li);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initView();
        initControl();
    }
    private void initControl() {
        themsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent them = new Intent(getApplicationContext(), ThemSanPhamActivity.class);
                startActivity(them);
            }
        });
    }

    private void initView() {
        themsp = findViewById(R.id.layout_add);
    }
}