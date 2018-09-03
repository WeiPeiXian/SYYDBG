package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class typeAdapter extends BaseRecyclerViewAdapter{
    public typeAdapter(Context context, List<?> list) {
        super(context, list);
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_flow_list;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final AVObject item = (AVObject) list.get(position);
        TextView dateTV = holder.getTextView(R.id.news_time_iv);
        TextView contentTV = holder.getTextView(R.id.news_message_iv);
        final SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
        Date curDate = item.getCreatedAt();
        String str = formatter.format(curDate);
        dateTV.setText(str);
        contentTV.setText(item.getString("name"));
    }
}