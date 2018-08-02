package com.example.weipeixian.MYYDBG.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.newsAdapter;

import java.util.List;

public class news extends Activity{
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    private newsAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        //找到自己布局文件的recyclerView

        recyclerView = (RecyclerView)findViewById(R.id.news_recycleView);

        //以下四行何以复制
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ///     以下四行代码仅供测试使用
        AVObject object = new AVObject("News");
        object.put("Title", "ssssssssssssss");
        object.put("body", "Ccesghi");
        object.saveInBackground();

        //获取对象列表
        AVQuery<AVObject> query = new AVQuery<>("news");
        query.whereExists("Name");
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
