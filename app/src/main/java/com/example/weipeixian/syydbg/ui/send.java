package com.example.weipeixian.syydbg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weipeixian.syydbg.BaseActivity;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.model.ChatListData;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

public class send extends BaseActivity {
    private EditText sentperson;
//    private TextView topNameTV;
    private Button sendBtn;
    private EditText sendcontent;
    private String sentname;
    private String content;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_send);
        initView();
        initEvent();

    }
    private void initEvent() {
        //监听发送按钮
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 发送
                sentname= sentperson.getText().toString();
                content = sendcontent.getText().toString();
                if (sentname==null){
                    Toast.makeText(send.this,"收件人不能为空",Toast.LENGTH_SHORT);
                }else if (content==null){
                    Toast.makeText(send.this,"发送内容不能为空",Toast.LENGTH_SHORT);
                }
                else{
                    sendMessageHX(sentname,content);
                }
            }
        });
        //监听取消按钮
//        backIV.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // 返回
//                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
//                intent.putExtra("data","refresh");
//                LocalBroadcastManager.getInstance(ChatDetailActivity.this).sendBroadcast(intent);
//                sendBroadcast(intent);
//                finish();
//            }
//        });
    }
    private void initView() {
        sentperson = (EditText) findViewById(R.id.sendperson);
        sendcontent = (EditText) findViewById(R.id.chat_content);
        sendBtn = (Button) findViewById(R.id.chat_send_btn);
    }
    void sendMessageHX(String username, final String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, username);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                startActivity(new Intent(send.this,
                        ChatDetailActivity.class).putExtra("userid", sentname));
                finish();
                Log.i("weipeixian", "send message on success");
            }
            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                Toast.makeText(send.this, s,  Toast.LENGTH_LONG).show();



                Log.i("weipeixian", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调

            }
        });
    }
}
