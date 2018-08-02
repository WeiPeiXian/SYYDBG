package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RecyclerHolder;
import com.example.weipeixian.MYYDBG.util.DateUtils;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class documentAdapter extends BaseRecyclerViewAdapter {
    public documentAdapter(Context context, List<?> list) {
        super(context, list);
    }
    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_document_file;
    }
    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        final AVObject  item = (AVObject) list.get(position);
        TextView contentTV = holder.getTextView(R.id.doc_name);
        TextView dateTV = holder.getTextView(R.id.doc_time);
        ImageView view = holder.getImageView(R.id.image);
        String type =  item.getString("Type");
        if (type.equals("file")){
            Log.d("s","file");
            view.setBackgroundResource(R.drawable.file);
        }
        else{
            Log.d("s","folder");
            view.setBackgroundResource(R.drawable.folder);
        }

        String name = item.getString("Name");
        Date date = item.getCreatedAt();
        if (date !=null)
            dateTV.setText(DateUtils.formatDataTest(date));
        contentTV.setText(name);
    }
}
