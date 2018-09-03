package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RVHolder;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;
import com.example.weipeixian.MYYDBG.model.SmsInfo;
import com.example.weipeixian.MYYDBG.ui.Fragment.message.SMSFragment;

import java.util.ArrayList;
import java.util.List;

public class SmsListAdapter extends BaseRecyclerViewAdapter implements Filterable {
    private List<SmsInfo> mSourceList;
    private List<SmsInfo> mFilterList;
    public SmsListAdapter(Context context, List<SmsInfo> list) {
        super(context, list);
        mSourceList = list;
        mFilterList = list;
    }
    public List<SmsInfo> getmFilterList(){
        return mFilterList;
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_sms_list;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        View view =  holder.getConvertView();
        final SmsInfo item = (SmsInfo) mFilterList.get(position);
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
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilterList = mSourceList;
                } else {
                    List<SmsInfo> filteredList = new ArrayList<>();
                    for (SmsInfo str : mSourceList) {
                        //这里根据需求，添加匹配规则
                        if (str.getMessage().contains(charString)) {
                            filteredList.add(str);
                        }else if (str.getContactMes().contains(charString)){
                            filteredList.add(str);
                        }else if(str.getDate().contains(charSequence)){
                            filteredList.add(str);
                        }else if(str.getPhone().contains(charSequence)){
                            filteredList.add(str);
                        }
                    }
                    mFilterList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<SmsInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
    @Override
    public int getItemCount() {
        return mFilterList.size();

    }
}
