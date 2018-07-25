package com.example.weipeixian.syydbg.holder;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecyclerHolder {
    private SparseArray<View> viewHolder;
    private View view;

    public static RecyclerHolder getViewHolder(View view) {
        RecyclerHolder viewHolder = (RecyclerHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new RecyclerHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    public RecyclerHolder(View view) {
        this.view = view;
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {
        return get(id);
    }

    public Button getButton(int id) {
        return get(id);
    }

    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }

    public RecyclerView getRecycleview(int id) {
        return get(id);
    }

    public LinearLayout getLinearLayout(int id) {
        return get(id);
    }
    public RelativeLayout getRelativeLayout(int id) {
        return get(id);
    }

}