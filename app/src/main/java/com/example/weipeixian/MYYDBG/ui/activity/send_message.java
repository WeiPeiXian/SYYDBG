package com.example.weipeixian.MYYDBG.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.CustomUserProvider;
import com.example.weipeixian.MYYDBG.R;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.LCChatKitUser;
import cn.leancloud.chatkit.LCChatProfilesCallBack;
import cn.leancloud.chatkit.event.LCIMInputBottomBarEvent;
import cn.leancloud.chatkit.event.LCIMInputBottomBarTextEvent;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.view.LCIMInputBottomBar;
import de.greenrobot.event.EventBus;

public class send_message extends BaseActivity{
    private EditText sendperson;
    protected LCIMInputBottomBar inputBottomBar;
    private EditText content;
    private ImageButton send;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);
        sendperson = (EditText)findViewById(R.id.sendperson);
        send  =(ImageButton)findViewById(R.id.send);
        content =(EditText)findViewById(R.id.chat_content);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = content.getText().toString();
                name = sendperson.getText().toString();
                if (name==null){
                    Toast.makeText(send_message.this,"接收人不能为空",Toast.LENGTH_SHORT).show();
                }
                else if (value==null){
                    Toast.makeText(send_message.this,"内容",Toast.LENGTH_SHORT).show();
                }
                else{
                    send_message(name,value);
                }

            }
        });
    }
    public void send_message(String name, String content){
        Intent intent = new Intent(this,conversation.class);
        List<String> list = new ArrayList<String>();
        intent.putExtra(LCIMConstants.PEER_ID, name);
        intent.putExtra("content",content);
        intent.putExtra("name",name);
        startActivity(intent);
        finish();
    }
}
