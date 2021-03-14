package com.example.chapter3.homework;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlaceholderFragment extends Fragment {

    private View target;
    private View target2;
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO ex3-3: 修改 fragment_placeholder，添加 loading 控件和列表视图控件
        super.onCreate(savedInstanceState);
        target = container.findViewById(R.id.animation_view);
        target2 = container.findViewById(R.id.recView);

        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            data.add("联系人" + i);
        }
        View view= inflater.inflate(R.layout.fragment_placeholder, container, false);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mAdapter = new MyAdapter(data);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recView);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 这里会在 5s 后执行
                // TODO ex3-4：实现动画，将 lottie 控件淡出，列表数据淡入
                @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator4 = ObjectAnimator.ofFloat(target,
                        "alpha",
                        1.0f,
                        0.0f);
                animator4.setDuration(1000);
                animator4.setRepeatCount(0);
                animator4.setRepeatMode(ObjectAnimator.REVERSE);
                @SuppressLint("ObjectAnimatorBinding") ObjectAnimator animator1 = ObjectAnimator.ofFloat(target2,
                        "alpha",
                        0.0f,
                        1.0f);
                animator1.setDuration(1000);
                animator1.setRepeatCount(0);
                animator1.setRepeatMode(ObjectAnimator.REVERSE);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(animator4, animator1);
                animatorSet.start();
            }
        }, 5000);
    }
}
