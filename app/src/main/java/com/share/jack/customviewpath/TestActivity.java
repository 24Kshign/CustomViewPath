package com.share.jack.customviewpath;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.share.jack.customviewpath.adapter.TestAdapter;
import com.share.jack.customviewpath.util.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 17/4/22
 */

public class TestActivity extends Activity {

    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.at_recyclerview);
        adapter = new TestAdapter(this, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewDivider(this));
        recyclerView.setAdapter(adapter);

        initData();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            list.add("24K纯帅" + i);
        }
        adapter.notifyDataSetChanged();
    }
}
