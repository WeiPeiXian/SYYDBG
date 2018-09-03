package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.processAdapter;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
public class process_manage extends BaseActivity {
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    private processAdapter itemAdapter;
    @BindView(R.id.add)
    Button add;
    @BindView(R.id.return4)
    ImageButton back;
    @BindView(R.id.addd)
    ImageButton addd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_manage);
        ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(process_manage.this, add_process.class));
            }
        });
        addd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(process_manage.this, add_process_type.class));
            }
        });
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
                    itemAdapter = new processAdapter(process_manage.this,list);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(process_manage.this,process_detail.class).putExtra("id",list.get(position).getObjectId()));
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            final String objid  = list.get(position).getObjectId();
                            android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(process_manage.this)
                                .setTitle("操作").setItems(new String[]{"确认删除？"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                AVQuery.doCloudQueryInBackground("delete from Process where objectId='"+objid+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                                    @Override
                                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                                        if(e == null){
                                                            update();
                                                        }
                                                        else {
                                                            Toast.makeText(process_manage.this,"删除失败",Toast.LENGTH_SHORT).show();
                                                            System.out.println(e.getMessage());
                                                        }
                                                    }
                                                });
                                                break;
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
