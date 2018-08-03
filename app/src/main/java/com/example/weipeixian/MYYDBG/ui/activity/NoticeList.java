package com.example.weipeixian.MYYDBG.ui.activity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.noticeAdapter;


import java.util.List;

public class NoticeList extends BaseActivity{
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    private List<AVObject> mylist;
    private noticeAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载自己的布局文件
        setContentView(R.layout.annoucement);
        //找到自己布局文件的recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.announce_recycleView);

        //对布局文件的操作
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        ImageButton btn = (ImageButton) findViewById(R.id.edit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NoticeList.this, newNotice.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        refresh();
    }
    public void refresh(){
        AVQuery<AVObject> query = new AVQuery<>("Notice");
        query.whereExists("N_title");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if (e != null) {
                    System.out.print(e.getMessage());
                } else {
                    mylist = list;
                    itemAdapter = new noticeAdapter(NoticeList.this, list);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void
                        onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent();
                            Bundle bd =new Bundle();
                            mylist.get(i);

                            //获取点击当前公告的属性
                            final AVObject item = (AVObject) mylist.get(i);
                            String new_title= (String) item.get("N_title");
                            String new_time= (String) item.get("N_time");
                            String new_matter= (String) item.get("N_matter");
                            String object_Id = item.getObjectId();
//                            //测试用例
//                            Log.d("test","click"+object_Id);
                            //传递参数给下个页面
                            intent.setClass(NoticeList.this, NoticeDetail.class);
                            intent.putExtra("extra_title",new_title);
                            intent.putExtra("extra_time",new_time);
                            intent.putExtra("extra_matter",new_matter);
                            intent.putExtra("extra_objectId",object_Id);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(itemAdapter);

                }
            }
        });
        recyclerView.setHasFixedSize(true);

    }
//    //点击函数
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.response:
//                Intent intent = new Intent();
//                intent.setClass(news.this, NoticeDetail.class);
//                startActivity(intent);
//                break;
//        }
//    }

}
