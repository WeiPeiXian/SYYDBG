package com.example.weipeixian.syydbg.ui.Fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.weipeixian.syydbg.BaseFragment;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.model.SmsInfo;
import com.example.weipeixian.syydbg.util.SmsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSFragment extends BaseFragment{
    RecyclerView recyclerView;
    List<SmsInfo> nameList = new ArrayList<>();
    private Button send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        send = (Button) view.findViewById(R.id.send);
        mHandler =new SmsHandler(mContext);
        initRecyclerview();
        recyclerView = (RecyclerView) view.findViewById(R.id.sms_recycleView);
//        recyclerView.setAdapter(adapter);
        return view;
    }
    private void initRecyclerview(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    private class  mhandler extends SmsHandler{

        public mhandler(Context context) {
            super(context);
        }
        public void handleMessage(Message msg){
            super.handleMessage(msg);

        }
    }
}
