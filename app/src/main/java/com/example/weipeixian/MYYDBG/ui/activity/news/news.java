package com.example.weipeixian.MYYDBG.ui.activity.news;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;

import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.newsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class news extends BaseActivity {
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    @BindView(R.id.return4)
    ImageButton back;
    @BindView(R.id.new_phone_message)
    ImageButton add;
    private newsAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news);
        //找到自己布局文件的recyclerView
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(news.this, addNews.class));
            }
        });
        setTitle("新闻");
        recyclerView = (RecyclerView)findViewById(R.id.news_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    public void onResume(){
        super.onResume();
        update();
    }
    public void update(){
        AVQuery<AVObject> query = new AVQuery<>("News");
        query.whereExists("title");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e!=null){
                    System.out.print(e.getMessage());
                }
                else {
                    final List<AVObject> mlist =new  ArrayList<>();
                    for (AVObject avObject:list){
                        if (avObject.getBoolean("zhiding")){
                            mlist.add(avObject);
                        }
                    }
                    for (AVObject avObject:list){
                        if (!avObject.getBoolean("zhiding")){
                            mlist.add(avObject);
                        }
                    }
                    itemAdapter = new newsAdapter(news.this,mlist);
                    recyclerView.setAdapter(itemAdapter);


                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AVObject news=mlist.get(i);
                            startActivity(new Intent(news.this, newsDetail.class).putExtra("id",news.getObjectId()));
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                            final AVObject avObject = mlist.get(position);
                            final boolean x = avObject.getBoolean("zhiding");
                            String[] s;
                            if (avObject.getBoolean("zhiding")){
                                 s = new String[]{"取消置顶"};
                            }
                            else {
                                s= new String[]{"置顶"};
                            }
                            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(news.this)
                                    .setTitle("操作").setItems(s, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which){
                                                case 0:
                                                    avObject.put("zhiding",!x);
                                                    avObject.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(AVException e) {
                                                            update();
                                                        }
                                                    });

                                            }

                                        }
                                    }).create();
                            dialog.setCanceledOnTouchOutside(true);
                            dialog.show();
                            return false;
                        }
                    });
                }
            }
        });
        recyclerView.setHasFixedSize(true);
    }


}
