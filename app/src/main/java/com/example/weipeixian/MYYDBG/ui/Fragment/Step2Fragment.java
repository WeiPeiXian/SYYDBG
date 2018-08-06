package com.example.weipeixian.MYYDBG.ui.Fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;

import androidx.navigation.Navigation;

public class Step2Fragment extends BaseFragment {
    public Step2Fragment() {
        // Required empty public constructor
    }
    private Button add;
    private LayoutInflater myinflater;
    private LinearLayout linear;
    private View view;
    final int[] i = new int[1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myinflater = inflater;
        view = inflater.inflate(R.layout.add_process_frament, container, false);
        i[0]=1;
        return view;

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add = view.findViewById(R.id.add);
        linear=(LinearLayout) view.findViewById(R.id.line);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addview();
            }
        });
    }


    public void addview(){
        final View myview = myinflater.inflate(R.layout.add_view, null);
        TextView name = myview.findViewById(R.id.name);
        TextView type =myview.findViewById(R.id.type);
        name.setText("新流程"+i[0]);
        i[0]++;
        type.setText("测试");
        linear.addView(myview);
        myview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear.removeView(myview);
            }
        });
    }


}
