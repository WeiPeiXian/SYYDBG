package com.example.weipeixian.MYYDBG.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.newsAdapter;

import java.util.List;

public class news extends AppCompatActivity {
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    private Toolbar toolbar;

    private newsAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        //找到自己布局文件的recyclerView
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle("新闻");
        recyclerView = (RecyclerView)findViewById(R.id.news_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        AVObject object = new AVObject("News");
        object.put("Title", "新闻列表");
        object.put("body", "新闻内容");
        object.put("time", "当前时间");
        object.put("type", "新闻类别");
        object.saveInBackground();

        //获取对象列表


    }
    @Override
    public void onResume(){
        super.onResume();
        update();
    }
    public void update(){
        AVQuery<AVObject> query = new AVQuery<>("News");
        query.whereExists("Title");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e!=null){
                    System.out.print(e.getMessage());
                }
                else {
                    itemAdapter = new newsAdapter(news.this,list);
                    recyclerView.setAdapter(itemAdapter);
                }
            }
        });
        recyclerView.setHasFixedSize(true);
    }

}
