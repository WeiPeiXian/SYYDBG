package com.example.weipeixian.MYYDBG.ui.activity.message;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.ShowSessionMessagesAdapter;

import android.os.Bundle;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowSessionMessagesActivity extends BaseActivity {
    private ListView sessionMessagesListView;
    @BindView(R.id.return4)
    ImageButton back;
    @BindView(R.id.send)
    ImageButton send;
    @BindView(R.id.chat_content)
    EditText content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_session_message);
        ButterKnife.bind(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_message(content.getText().toString());
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //使用Intent对象得到短信会话的id
        Intent intent = getIntent();
        String threadId = intent.getStringExtra("threadId");

        sessionMessagesListView = (ListView)this.findViewById(R.id.SessionMessageListView);

        ShowSessionMessagesAdapter sessionMessagesAdapter = new ShowSessionMessagesAdapter(this);
        sessionMessagesAdapter.getSessionMessages(threadId);
        sessionMessagesListView.setAdapter(sessionMessagesAdapter);

        //实时通知数据已更新
        sessionMessagesAdapter.notifyDataSetChanged();

    }
    public void send_message(String m){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            Log.d(this.getIntent().getStringExtra("phone"),content.getText().toString());
            smsManager.sendTextMessage(this.getIntent().getStringExtra("phone"), null, content.getText().toString(), null, null);
            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
            ShowSessionMessagesAdapter sessionMessagesAdapter = new ShowSessionMessagesAdapter(this);
            Intent intent = getIntent();
            String threadId = intent.getStringExtra("threadId");
            sessionMessagesAdapter.getSessionMessages(threadId);
            sessionMessagesListView.setAdapter(sessionMessagesAdapter);

            //实时通知数据已更新
            sessionMessagesAdapter.notifyDataSetChanged();

            content.setText("");

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "发送失败",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}