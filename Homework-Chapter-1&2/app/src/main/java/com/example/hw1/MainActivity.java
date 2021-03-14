package com.example.hw1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //recyclerView 三件套
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText mEditText;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //绑定主界面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //得到每一行的文字，并存入Array
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            data.add("这是第" + i + "行");
        }
        //绑定recyclerView
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new MyAdapter(data);
        mRecyclerView = (RecyclerView) findViewById(R.id.recView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        //EditText搜索框
        mEditText = (EditText) findViewById(R.id.editText);
        mEditText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = mEditText.getText().toString().trim();
                ArrayList<String> filters = new ArrayList<>();
                for (String line : data) {
                    if (line.contains(str)) {
                        filters.add(line);
                    }
                }
                mAdapter.updateData(filters);
            }
        });
    }
    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 101; i++) {
            data.add("这是第" + i + "行");
        }
        return data;
    }
}