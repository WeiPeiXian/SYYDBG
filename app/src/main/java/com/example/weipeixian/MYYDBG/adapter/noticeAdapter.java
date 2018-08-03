package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;

import java.util.List;

public class noticeAdapter extends BaseRecyclerViewAdapter{


    public noticeAdapter(Context context, List<?> list) {
        super(context, list);
    }
    @Override
    //改为自己的布局文件
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_noticement;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final AVObject item = (AVObject) list.get(position);
        TextView dateTV = holder.getTextView(R.id.time);
        TextView contentTV = holder.getTextView(R.id.title);
        dateTV.setText(item.getString("N_time"));
        contentTV.setText(item.getString("N_title"));
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

}
