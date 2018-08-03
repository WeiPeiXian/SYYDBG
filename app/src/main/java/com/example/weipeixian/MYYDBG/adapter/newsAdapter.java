package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;

import java.util.List;

public class newsAdapter extends BaseRecyclerViewAdapter{
    public newsAdapter(Context context, List<?> list) {
        super(context, list);
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_news_list;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final AVObject item = (AVObject) list.get(position);
        TextView dateTV = holder.getTextView(R.id.news_time_iv);
        TextView contentTV = holder.getTextView(R.id.news_message_iv);
        dateTV.setText(item.getString("Title"));
        contentTV.setText(item.getString("body"));
    }
}
