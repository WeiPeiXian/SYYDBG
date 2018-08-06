package com.example.weipeixian.MYYDBG.ui.activity;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.PermissionListener;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.contactadapter;
import com.example.weipeixian.MYYDBG.adapter.newsAdapter;
import com.example.weipeixian.MYYDBG.model.PhoneInfo;
import com.example.weipeixian.MYYDBG.model.SmsInfo;
import com.example.weipeixian.MYYDBG.util.PhoneUtil;

import java.util.List;

public class contacts extends BaseActivity {
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    private Toolbar toolbar;

    private contactadapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        //找到自己布局文件的recyclerView
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("联系人");
        recyclerView = (RecyclerView)findViewById(R.id.news_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(contacts.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        requestPermission();

        //获取对象列表


    }
    private void requestPermission(){
        requestRunTimePermission(new String[]{Manifest.permission.READ_CONTACTS}
                , new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onGranted() {  //所有权限授权成功
                        get();
                    }
                    @Override
                    public void onGranted(List<String> grantedPermission) { //授权失败权限集合
                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) { //授权成功权限集合
                    }
                });
    }
    public void get(){
        PhoneUtil phoneUtil = new PhoneUtil(contacts.this);
        List<PhoneInfo> list =  phoneUtil.getPhone();
        itemAdapter = new contactadapter(contacts.this,list);
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setHasFixedSize(true);

    }

}
