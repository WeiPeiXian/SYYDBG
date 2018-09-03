package com.example.weipeixian.MYYDBG.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.CustomUserProvider;
import com.example.weipeixian.MYYDBG.PermissionListener;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.activity.contact.contactHostActivity;
import com.example.weipeixian.MYYDBG.ui.activity.document.document;
import com.example.weipeixian.MYYDBG.ui.activity.message.SMSHostActivity;
import com.example.weipeixian.MYYDBG.ui.activity.news.news;
import com.example.weipeixian.MYYDBG.ui.activity.notice.NoticeList;
import com.example.weipeixian.MYYDBG.ui.activity.process.process;
import com.example.weipeixian.MYYDBG.util.XmTools;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

public class open_page extends BaseActivity {
    private ImageButton[] buttons = new ImageButton[6];
    private ImageButton button;
    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_page);
        button = (ImageButton) findViewById(R.id.edit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVUser.logOut();
                startActivity(new Intent(open_page.this, LoginActivity.class));
                finish();
            }
        });
        buttons[0] = (ImageButton) findViewById(R.id.bt_sms);
        buttons[1] = (ImageButton) findViewById(R.id.bt_contact);
        buttons[2] = (ImageButton) findViewById(R.id.bt_announcement);
        buttons[3] = (ImageButton) findViewById(R.id.bt_news);
        buttons[4] = (ImageButton) findViewById(R.id.bt_document);
        buttons[5] = (ImageButton) findViewById(R.id.bt_order);
        final Intent[] it = new Intent[6];
        it[0] = new Intent(open_page.this, SMSHostActivity.class);
        it[1] = new Intent(open_page.this, contactHostActivity.class);
        it[2] = new Intent(open_page.this, NoticeList.class);
        it[3] = new Intent(open_page.this, news.class);
        it[4] = new Intent(open_page.this, document.class);
        it[5] = new Intent(open_page.this, process.class);
        for (int i = 0; i < 6; i++) {
            final int finalI = i;
            final int finalI1 = i;
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
