package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class InsideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside);

        int num = getIntent().getIntExtra("num", 0);
        String s = "第" + num + "行";

        TextView txView = findViewById(R.id.insideTx);
        txView.setText(s);
    }
}