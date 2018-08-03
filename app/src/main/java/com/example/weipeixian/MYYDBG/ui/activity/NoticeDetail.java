package com.example.weipeixian.MYYDBG.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.noticeAdapter;

import java.util.List;

public class NoticeDetail extends Activity {
    protected RecyclerView recyclerView;
    //下面的adapter换为自己的
    private List<AVObject> mylist;
    private noticeAdapter itemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
// TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //加载自己的布局文件
        this.setContentView(R.layout.other);
        //找到自己布局文件的recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.news_recycleView_2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

       //从上个页面中获取数据
        Intent intent = getIntent();
        String data_title = intent.getStringExtra("extra_title");
        String data_time = intent.getStringExtra("extra_time");
        String data_matter = intent.getStringExtra("extra_matter");
//        //测试用例
//        Log.d("test","click"+data_objectId);
        //将得到的数据显示在布局中
        TextView newNotice_title =(TextView)findViewById(R.id.news_message_iv_2);
        TextView newNotice_time =(TextView)findViewById(R.id.news_time_iv_2);
        TextView newNotice_matter =(TextView)findViewById(R.id.Notice_matter_iv_2);
        newNotice_title.setText(data_title);
        newNotice_time.setText(data_time);
        newNotice_matter.setText(data_matter);

        recyclerView.setAdapter(itemAdapter);
        recyclerView.setHasFixedSize(true);

    }
    //返回的点击事件1
    public void ImageButtonClick1(View v) {
        switch (v.getId()) {
            case R.id.return3:
                finish();
                break;
        }
    }
    //返回的点击事件2
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

                            // 执行 CQL 语句实现删除一个 Todo 对象
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
//            startActivity(new Intent(this, NewActivity.class));
                    //提示框
                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(NoticeDetail.this);
                    alterDialog.setMessage("确认修改？");
                    alterDialog.setCancelable(false);
                    //确认函数
                    alterDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(NoticeDetail.this, "确认", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            //获取上个页面的参数
                            String data_title = intent.getStringExtra("extra_title");
                            String data_time = intent.getStringExtra("extra_time");
                            String data_matter = intent.getStringExtra("extra_matter");
                            String data_objectId = intent.getStringExtra("extra_objectId");
                            //传递参数给下个页面
                            intent.setClass(NoticeDetail.this, ChangeNotice.class);
                            intent.putExtra("extra_title",data_title);
                            intent.putExtra("extra_time",data_time);
                            intent.putExtra("extra_matter",data_matter);
                            intent.putExtra("extra_objectId",data_objectId);
                            startActivity(intent);
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
                }
                return false;
            }
        });
        popupMenu.show();
    }
}