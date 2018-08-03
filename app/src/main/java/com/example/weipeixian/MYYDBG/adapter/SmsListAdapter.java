package com.example.weipeixian.MYYDBG.adapter;

import android.app.Fragment;
import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;
import com.example.weipeixian.MYYDBG.model.SmsInfo;
import com.example.weipeixian.MYYDBG.ui.Fragment.SMSFragment;

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
        View view =  holder.getConvertView();
        SMSFragment.cancle(view, SMSFragment.tcontext,SMSFragment.ed);
        final SmsInfo item = (SmsInfo) list.get(position);
        TextView dateTV = holder.getTextView(R.id.sms_time_iv);
        TextView contentTV = holder.getTextView(R.id.sms_message_iv);
        TextView nameTV = holder.getTextView(R.id.sms_name_iv);
        dateTV.setText(item.getDate());
        try {
            contentTV.setText(Html.fromHtml(item.getMessage()));
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        nameTV.setText(item.getContactMes());
    }
}
