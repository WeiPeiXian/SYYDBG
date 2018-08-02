package com.example.weipeixian.MYYDBG.holder;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class RVHolder extends RecyclerView.ViewHolder {

    private RecyclerHolder viewHolder;
    public RVHolder(View itemView) {
        super(itemView);
        viewHolder= RecyclerHolder.getViewHolder(itemView);
    }
    public RecyclerHolder getViewHolder() {
        return viewHolder;
    }
}