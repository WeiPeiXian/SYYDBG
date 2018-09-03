package com.example.weipeixian.MYYDBG.ui.activity.notice;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.GetCallback;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.noticeAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoticeDetail extends Activity {
    //下面的adapter换为自己的
    final SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd");
    String m;
    private List<AVObject> mylist;
    private noticeAdapter itemAdapter;
    String endtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.notice_detail);
        Intent intent = getIntent();
        m = this.getIntent().getStringExtra("extra_objectId");
    }
    @Override
    public void onResume() {
        super.onResume();
        final TextView newNotice_title =(TextView)findViewById(R.id.news_message_iv_2);
        final TextView newNotice_time =(TextView)findViewById(R.id.news_time_iv_2);
        final TextView newNotice_matter =(TextView)findViewById(R.id.Notice_matter_iv_2);
        AVQuery<AVObject> query =new AVQuery<>("Notice");
        query.getInBackground(m, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    newNotice_title.setText(avObject.getString("N_title"));
                    Date date = avObject.getCreatedAt();
                    String str = formatter.format(date);
                    newNotice_time.setText(str);
                    newNotice_matter.setText(avObject.getString("N_matter"));
                }
                else {
                    Log.d("error",e.getMessage());
                }
            }
        });
    }
    //返回的点击事件1
    public void ImageButtonClick1(View v) {
        switch (v.getId()) {
            case R.id.return3:
                finish();
                break;
        }
    }
    public void ImageButtonClick2(View v) {
        switch (v.getId()) {
            case R.id.new_phone_message:
                final ImageButton button = (ImageButton) findViewById(R.id.new_phone_message);
                showPopupMenu(button);
                break;
        }
    }
    private void showPopupMenu(View view) {
        // View当前PopupMenu显示的相对View的位置
        PopupMenu popupMenu = new PopupMenu(this, view);
        // menu布局
        popupMenu.getMenuInflater().inflate(R.menu.option_menu, popupMenu.getMenu());
        // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_unKnow) {  //根据ItemId进行判断。
                    //提示框
                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(NoticeDetail.this);
                    alterDialog.setMessage("确认删除？");
                    alterDialog.setCancelable(false);
                    //确认函数
                    alterDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(NoticeDetail.this, "确认", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            String data_objectId = intent.getStringExtra("extra_objectId");
                            AVQuery.doCloudQueryInBackground("delete from Notice where objectId='"+data_objectId+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    // 如果 e 为空，说明删除成功
                                    if(e == null){
                                        //删除后跳转页面
                                        finish();
                                    }
                                    else {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            });
                        }
                    });
                    //取消函数
                    alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(NoticeDetail.this, "操作取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alterDialog.show();
                    return true;
                } else if (item.getItemId() == R.id.action_startActivity) {
                    Intent intent = getIntent();
                    String data_objectId = intent.getStringExtra("extra_objectId");
                    intent.putExtra("id",data_objectId);
                    intent.setClass(NoticeDetail.this, ChangeNotice.class);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}