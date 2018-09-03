package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.processAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class add_process_type extends BaseActivity {

    AVObject mavObject;
    List<String> list;
    @BindView(R.id.return4)
    ImageButton back;
    @BindView(R.id.new_phone_message)
    ImageButton add;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycleView)
    RecyclerView recyclerView;
    private processAdapter itemAdapter;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_select);
        ButterKnife.bind(this);
        title.setText("流程类别管理");

        add.setVisibility(View.VISIBLE);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et=new EditText(add_process_type.this);
                et.setHint("类型名称");
                AlertDialog dialog = new AlertDialog.Builder(add_process_type.this)
                        .setTitle("新增流程类型")
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                list.add(et.getText().toString());
                                mavObject.put("type",list);
                                mavObject.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(AVException e) { if (e == null)
                                        { update();
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
                                //不做任何事
                            }
                        }).create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);
            }
        });
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
        String objid = "5b6d21179f54540035076027";
        AVQuery<AVObject> query = new AVQuery<>("processType");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @Override
            public void done(final AVObject avObject, AVException e) {
                mavObject = avObject;
                final List<String> lists = avObject.getList("type");
                list =lists;
                final List<AVObject> list =new ArrayList<>();
                for (String m:lists){
                    AVObject x =new AVObject();
                    x.put("name",m);
                    list.add(x);
                }
                itemAdapter = new processAdapter(add_process_type.this,list);
                recyclerView.setAdapter(itemAdapter);
                itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        AlertDialog dialog = new AlertDialog.Builder(add_process_type.this)
                                .setTitle("确认删除类型?")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        lists.remove(position);
                                        mavObject.put("type",lists);
                                        mavObject.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
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
        });
        recyclerView.setHasFixedSize(true);
    }


}
