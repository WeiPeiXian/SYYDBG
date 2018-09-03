package com.example.weipeixian.MYYDBG.ui.activity.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.View.MyImageView;
import com.example.weipeixian.MYYDBG.adapter.commentAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class newsDetail extends Activity {
    protected RecyclerView recyclerView;
    protected commentAdapter itemAdapter;
    String objid;
    private ProgressDialog pd;
    private AVObject news;
    private List<AVObject> mylist;
    private EditText comment;
    @BindView(R.id.return1)
    ImageButton back;
    @BindView(R.id.edit)
    ImageButton edit;
    @BindView(R.id.send)
    ImageButton send;
    @BindView(R.id.chat_content)

    EditText mcontent;
    @BindView(R.id.img)
    MyImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.news_detail);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s= mcontent.getText().toString();
                AVObject avObject = new AVObject("Comment");
                avObject.put("newid",objid);
                avObject.put("comment",s);
                avObject.put("user", AVUser.getCurrentUser().getUsername());
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null){
                            update_comment();
                            mcontent.setText("");
                            int x = news.getInt("count");
                            news.put("count",x+1);
                            news.saveInBackground();
                        }
                    }
                });
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(newsDetail.this)
                        .setTitle("操作").setItems(new String[]{ "修改","删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        Intent intent = new Intent(newsDetail.this, newschange.class);
                                        intent.putExtra("id",objid);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        AVQuery.doCloudQueryInBackground("delete from News where objectId='"+objid+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                            @Override
                                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                               if (e ==null){
                                                   finish();
                                               }
                                            }
                                        });
                                        break;

                                }

                            }
                        }).create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        objid = this.getIntent().getStringExtra("id");


    }
    @Override
    public void onResume() {
        super.onResume();
        final TextView title =(TextView)findViewById(R.id.news_title_content);
        final TextView time =(TextView)findViewById(R.id.news_time_content);
        final TextView content =(TextView)findViewById(R.id.news_content_content1);
        AVQuery<AVObject> query =new AVQuery<>("News");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    news = avObject;
                    title.setText(avObject.getString("title"));
                    final SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
                    Date curDate = avObject.getCreatedAt();
                    String str = formatter.format(curDate);
                    time.setText(str);
                    content.setText(avObject.getString("content"));
                    RelativeLayout r1 = findViewById(R.id.news_comment);
                    AVFile file = avObject.getAVFile("picture");
                    if (file!=null){
                        String m = file.getThumbnailUrl(true, 700, 700);
                        Log.d("s",m);
                        img.setImageURL(m);
                        img.setVisibility(View.VISIBLE);
                    }
                    LinearLayout l = findViewById(R.id.bottom);
                    RelativeLayout r2 = findViewById(R.id.item);
                    if (avObject.getBoolean("pinglun")){
                        r1.setVisibility(View.VISIBLE);
                        r2.setVisibility(View.VISIBLE);
                        l.setVisibility(View.VISIBLE);
                        update_comment();
                    }
                    else {
                        r1.setVisibility(View.GONE);
                        r2.setVisibility(View.GONE);
                        l.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
    public void update_comment(){
        AVQuery<AVObject> query =new AVQuery<>("Comment");
        query.whereEqualTo("newid",objid);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                itemAdapter = new commentAdapter(newsDetail.this,list);
                recyclerView.setAdapter(itemAdapter);
                recyclerView.setHasFixedSize(true);
            }
        });
    }

}