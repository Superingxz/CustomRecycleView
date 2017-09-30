package com.myz.customrecycleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.myz.customrecycleview.adapter.MyRecycleViewAdapter;
import com.myz.mrecyclerview.universaladapter.recyclerview.CommonRecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CommonRecycleViewAdapter<String> adapter;
    private MyRecycleViewAdapter myRecycleViewAdapter;
    private List<String> list;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("text" + (i + 1));
        }
        myRecycleViewAdapter = new MyRecycleViewAdapter(list, MainActivity.this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(myRecycleViewAdapter);


    }
}
