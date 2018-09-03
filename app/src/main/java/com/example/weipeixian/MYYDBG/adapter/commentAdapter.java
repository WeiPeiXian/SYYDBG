package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;
import com.example.weipeixian.MYYDBG.model.ChatListData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class commentAdapter extends BaseRecyclerViewAdapter{
    public commentAdapter(Context context, List<?> list) {
        super(context, list);
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_comment;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final AVObject item = (AVObject) list.get(position);
        TextView dateTV = holder.getTextView(R.id.time);
        TextView title = holder.getTextView(R.id.title);
        TextView contentTV = holder.getTextView(R.id.content);
        final SimpleDateFormat formatter = new SimpleDateFormat ("MM-dd HH:mm:ss");
        Date curDate = item.getCreatedAt();
        String str = formatter.format(curDate);
        dateTV.setText(str);
        title.setText(item.getString("user"));
        contentTV.setText(item.getString("comment"));
    }
}
