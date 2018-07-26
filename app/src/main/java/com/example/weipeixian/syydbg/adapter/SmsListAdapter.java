package com.example.weipeixian.syydbg.adapter;

import android.content.Context;
import android.widget.TextView;

import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.holder.RecyclerHolder;
import com.example.weipeixian.syydbg.model.ConversationListData;
import com.example.weipeixian.syydbg.model.SmsInfo;

import java.util.List;

public class SmsListAdapter extends BaseRecyclerViewAdapter {
    public SmsListAdapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_sms_list;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final SmsInfo item = (SmsInfo) list.get(position);
        TextView dateTV = holder.getTextView(R.id.sms_time_iv);
        TextView contentTV = holder.getTextView(R.id.sms_message_iv);
        TextView nameTV = holder.getTextView(R.id.sms_name_iv);
        dateTV.setText(item.getTime());
        contentTV.setText(item.getMessage());
        nameTV.setText(item.getUsername());
    }



}
