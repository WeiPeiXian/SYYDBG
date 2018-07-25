package com.example.weipeixian.syydbg.adapter;

import android.content.Context;
import com.example.weipeixian.syydbg.holder.RecyclerHolder;

import java.util.List;

public class SmsListAdapter extends BaseRecyclerViewAdapter {

    public SmsListAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {

    }


}
