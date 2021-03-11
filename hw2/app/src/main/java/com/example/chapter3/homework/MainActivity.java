package com.example.chapter3.homework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView=(ListView)findViewById(R.id.list);
        List<String> list = new ArrayList<String>();
        list.add("西安市未央区");
        list.add("西安市未央区");
        list.add("西安市未央区");
        list.add("西安市未央区");
        list.add("西安市未央区");
        list.add("西安市未央区");
        ///可以一直添加，在真机运行后可以下拉列表
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        findViewById(R.id.btn_ex1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Ch3Ex1Activity.class));
            }
        });
        findViewById(R.id.btn_ex2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Ch3Ex2Activity.class));
            }
        });
        findViewById(R.id.btn_ex3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Ch3Ex3Activity.class));
            }
        });
    }
}
