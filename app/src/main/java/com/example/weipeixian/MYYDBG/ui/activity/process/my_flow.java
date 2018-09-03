package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.typeAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class my_flow extends BaseActivity {
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    @BindView(R.id.back)
    ImageButton back;
    private typeAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_flow);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        recyclerView = (RecyclerView)findViewById(R.id.recycleView);
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
        AVQuery<AVObject> query = new AVQuery<>("Flow");
        query.whereEqualTo("user", AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {

                if (e!=null){
                    System.out.print(e.getMessage());
                }
                else {
                    final List<AVObject> mlist =list;
                    itemAdapter = new typeAdapter(my_flow.this,mlist);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AVObject news=mlist.get(i);
                            startActivity(new Intent(my_flow.this, flowDetail.class).putExtra("id",news.getObjectId()).putExtra("id",news.getObjectId()));
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            final  String abjid = mlist.get(position).getObjectId();
                            AlertDialog dialog = new AlertDialog.Builder(my_flow.this)
                                    .setTitle("确认删除流程?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            AVQuery.doCloudQueryInBackground("delete from Flow where objectId='"+abjid+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                                @Override
                                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                                    if (e ==null){
                                                        update();
                                                    }
                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                        }
                                    }).create();
                            dialog.show();
                            dialog.setCanceledOnTouchOutside(true);



                            return false;
                        }
                    });


                }
            }
        });
        recyclerView.setHasFixedSize(true);
    }


}
