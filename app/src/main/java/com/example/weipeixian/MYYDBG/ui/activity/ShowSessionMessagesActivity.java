package com.example.weipeixian.MYYDBG.ui.activity;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.ShowSessionMessagesAdapter;

import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.widget.ListView;

public class ShowSessionMessagesActivity extends BaseActivity {
    private ListView sessionMessagesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_session_message);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.show_session_messages, menu);
//        return true;
//    }

}