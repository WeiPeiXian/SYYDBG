package com.example.weipeixian.MYYDBG.ui.Fragment.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.activity.message.send_message;

public class FragmentChat extends BaseFragment {
    private Toolbar toolbar;
    private final int BACK_INTERVAL = 1000;
    private View view;
    private boolean ShowKeyboard =false;
    private ImageButton back;
    private ImageButton send;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inner_message, container, false);
        back =view.findViewById(R.id.back);
        send = view.findViewById(R.id.add);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),send_message.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}