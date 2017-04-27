package com.share.jack.customviewpath.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.share.jack.customviewpath.R;

import java.util.List;

/**
 *
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {

    private Context mContext;
    private List<String> mList;

    public TestAdapter(Context mContext, List<String> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, parent, false));
    }

    @Override
    public void onBindViewHolder(TestViewHolder holder, int position) {
        holder.setContent(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return null != mList ? mList.size() : 0;
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        public TestViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.it_tv_content);
        }

        public void setContent(String content) {
            tvContent.setText(content);
        }
    }
}
