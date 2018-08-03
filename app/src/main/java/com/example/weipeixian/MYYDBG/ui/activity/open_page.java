package com.example.weipeixian.MYYDBG.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;

import com.avos.avoscloud.AVUser;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.util.XmTools;

import java.security.PrivateKey;

public class open_page extends BaseActivity{
    private ImageButton[] buttons = new ImageButton[6];
    private ImageButton button;
    private String[] data = { "SMS", "contacts", "NoticeList", "news",
            "document", "请假流程","退出" };
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_page);
        button = (ImageButton) findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();// 清除缓存用户对象
                startActivity(new Intent(open_page.this,LoginActivity.class));
                finish();
            }
        });
        buttons[0] =(ImageButton)findViewById(R.id.bt_sms);
        buttons[1]=(ImageButton)findViewById(R.id.bt_contact);
        buttons[2]=(ImageButton)findViewById(R.id.bt_announcement);
        buttons[3]=(ImageButton)findViewById(R.id.bt_news);
        buttons[4]=(ImageButton)findViewById(R.id.bt_document);
        buttons[5]=(ImageButton)findViewById(R.id.bt_order);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(open_page.this, android.R.layout.simple_list_item_1, data);
        final Intent[] it = new Intent[6];
        it[0] =new Intent(open_page.this,SMSHostActivity.class);
        it[1] =new Intent(open_page.this,contacts.class);
        it[2] =new Intent(open_page.this,NoticeList.class);
        it[3] =new Intent(open_page.this,news.class);
        it[4] =new Intent(open_page.this,document.class);
        it[5] =new Intent(open_page.this,process.class);
        for (int i=0;i<6;i++){
            final int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(it[finalI]);
                }
            });
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            XmTools.showExitDialog(open_page.this);
        }
        return super.onKeyDown(keyCode, event);
    }
}
