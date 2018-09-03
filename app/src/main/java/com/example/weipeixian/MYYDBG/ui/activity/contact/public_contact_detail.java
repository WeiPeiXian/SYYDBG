package com.example.weipeixian.MYYDBG.ui.activity.contact;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
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

public class public_contact_detail extends Activity{
    TextView newPhone_name;
    TextView newPhone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.contact_detail);
        newPhone_name = (TextView) findViewById(R.id.phone_name);
        newPhone_number = (TextView) findViewById(R.id.phone_number);

    }
    @Override
    public void onResume() {
        super.onResume();
        String data_objectId = getIntent().getStringExtra("extra_objectId");
        AVQuery<AVObject> query =new AVQuery<>("Public_Contact");
        query.getInBackground(data_objectId, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    newPhone_name.setText(avObject.getString("P_name"));
                    newPhone_number.setText(avObject.getString("P_number"));
                }
                else {
                }
            }
        });



    }
    public void ImageButtonClick(View v) {
        switch (v.getId()) {
            case R.id.return3:
                finish();
                break;
            case R.id.new_phone_message:
                final ImageButton button = (ImageButton) findViewById(R.id.new_phone_message);
                showPopupMenu(button);
                break;
        }
    }
    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.option_menu_contact, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_unKnow) {  //根据ItemId进行判断。
                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(public_contact_detail.this);
                    alterDialog.setMessage("确认删除？");
                    alterDialog.setCancelable(false);
                    alterDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(public_contact_detail.this, "确认", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            String data_objectId = intent.getStringExtra("extra_objectId");
                            AVQuery.doCloudQueryInBackground("delete from Public_Contact where objectId='"+data_objectId+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                @Override
                                public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                    if(e == null){
                                        finish();
                                    }
                                    else {
                                        System.out.println(e.getMessage());
                                    }
                                }
                            });
                        }
                    });
                    alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(public_contact_detail.this, "操作取消", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alterDialog.show();
                    return true;
                } else if (item.getItemId() == R.id.action_startActivity) {
                    Intent intent = getIntent();
                    String data_objectId = intent.getStringExtra("extra_objectId");
                    intent.setClass(public_contact_detail.this, Change_public_Contact.class);
                    intent.putExtra("extra_name",newPhone_name.getText().toString());
                    intent.putExtra("extra_number",newPhone_number.getText().toString());
                    intent.putExtra("extra_objectId",data_objectId);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }
}
