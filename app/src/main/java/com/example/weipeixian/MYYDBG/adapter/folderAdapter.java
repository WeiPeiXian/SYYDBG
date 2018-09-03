package com.example.weipeixian.MYYDBG.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.RVHolder;
import com.example.weipeixian.MYYDBG.util.DateUtils;
import java.util.Date;
import java.util.List;
public class folderAdapter extends RecyclerView.Adapter<RVHolder> {
    private int mSelectedPos=-1;
    public List<?> list;
    private Context context;
    public folderAdapter(Context context, List<?> list) {
        this.list = list;
        this.context=context;
    }
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_folder;
    }
    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(onCreateViewLayoutID(viewType), null);
        return new RVHolder(view);
    }

    @Override
    public void onBindViewHolder(RVHolder holder, int position) {
    }
    @Override
    public void onBindViewHolder(final RVHolder holder, final int position,List payloads) {
        if(payloads.isEmpty()){
            final AVObject  item = (AVObject) list.get(position);
            TextView contentTV = holder.getViewHolder().getTextView(R.id.doc_name);
            TextView dateTV = holder.getViewHolder().getTextView(R.id.doc_time);
            String name = item.getString("Name");
            Date date = item.getCreatedAt();
            dateTV.setText(DateUtils.formatDataTest(date));
            contentTV.setText(name);
            holder.getViewHolder().getCheckBox(R.id.image).setChecked(mSelectedPos==position);
        }else{
            holder.getViewHolder().getCheckBox(R.id.image).setChecked(mSelectedPos==position);
        }
        holder.getViewHolder().getCheckBox(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mSelectedPos!=position){
                    holder.getViewHolder().getCheckBox(R.id.image).setChecked(true);
                    if(mSelectedPos!=-1){
                        notifyItemChanged(mSelectedPos,0);
                    }
                    mSelectedPos=position;
                    Log.d("ss",String.valueOf(mSelectedPos));
                }
                else {
                    mSelectedPos = -1;
                    Log.d("ss",String.valueOf(mSelectedPos));

                }
            }
        });
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(null, v, holder.getPosition(), holder.getItemId());
                }
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClickListener.onItemLongClick(null, v, holder.getPosition(), holder.getItemId());
                    return true;
                }
            });
        }
    }
    public int getSelectedPos(){
        return mSelectedPos;
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }
    public AdapterView.OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }
    /**
     * 添加Item点击事件
     */
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**
     * 添加Item长按事件
     */
    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}
