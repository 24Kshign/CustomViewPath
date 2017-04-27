package com.share.jack.customviewpath;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.share.jack.customviewpath.adapter.TestAdapter;
import com.share.jack.customviewpath.widget.CustomPathMeasure;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class TestActivity extends Activity {

    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private List<String> list = new ArrayList<>();

    private CustomPathMeasure customPathMeasure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }

    private void initView() {
//        recyclerView = (RecyclerView) findViewById(R.id.at_recyclerview);
//        adapter = new TestAdapter(this, list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new RecyclerViewDivider(this));
//        recyclerView.setAdapter(adapter);

//        initData();
        customPathMeasure= (CustomPathMeasure) findViewById(R.id.at_path_measure);
        customPathMeasure.startAnim();
    }

    private void initData() {
        for (int i = 0; i < 20; i++) {
            list.add("24K纯帅" + i);
        }
        adapter.notifyDataSetChanged();
    }
}
