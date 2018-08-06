package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;
import com.example.weipeixian.MYYDBG.model.PhoneInfo;
import com.example.weipeixian.MYYDBG.model.SmsInfo;
import com.example.weipeixian.MYYDBG.ui.Fragment.SMSFragment;

import java.util.List;

public class contactadapter  extends BaseRecyclerViewAdapter {
    public contactadapter(Context context, List<?> list) {
        super(context, list);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_sms_list;
    }

    public void onBindViewHolder(RecyclerHolder holder, int position) {
        View view =  holder.getConvertView();
        final PhoneInfo item = (PhoneInfo) list.get(position);
        TextView contentTV = holder.getTextView(R.id.sms_message_iv);
        TextView nameTV = holder.getTextView(R.id.sms_name_iv);
        contentTV.setText(item.getTelPhone());
        nameTV.setText(item.getName());
    }
}
