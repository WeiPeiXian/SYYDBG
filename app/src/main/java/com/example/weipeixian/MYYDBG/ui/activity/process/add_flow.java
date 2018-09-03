package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.util.CacheActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.processAdapter;


import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class add_flow extends BaseActivity{
    //下面的adapter换为自己的
    private processAdapter itemAdapter;

    @BindView(R.id.return4)
    ImageButton back;
    @BindView(R.id.recycleView)

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_select);
        if (!CacheActivity.activityList.contains(add_flow.this)) {
            CacheActivity.addActivity(add_flow.this);
        }
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        update();
    }
    public void update(){
        AVQuery<AVObject> query = new AVQuery<>("Process");
        query.whereExists("name");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {

                if (e!=null){
                    System.out.print(e.getMessage());
                }
                else {
                    Log.d("s",list.toString());
                    itemAdapter = new processAdapter(add_flow.this,list);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AVObject avObject = list.get(i);
                            startActivity(new Intent(add_flow.this, new_flow.class).putExtra("id",avObject.getObjectId()));
                        }
                    });
                }
            }
        });
        recyclerView.setHasFixedSize(true);
    }
}
