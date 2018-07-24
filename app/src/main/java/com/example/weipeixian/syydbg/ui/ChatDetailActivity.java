package com.example.weipeixian.syydbg.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weipeixian.syydbg.BaseActivity;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.adapter.ChatListAdapter;
import com.example.weipeixian.syydbg.model.ChatListData;
import com.example.weipeixian.syydbg.util.LogUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

public class ChatDetailActivity extends BaseActivity implements EMMessageListener {

    private EditText contentET;
    private TextView topNameTV;
    private ImageView backIV;
    private Button sendBtn;
    private EMMessageListener mMessageListener;
    // 当前会话对象
    private EMConversation emConversation;
    private ListView mListView;
    private List<ChatListData> mListData = new ArrayList<ChatListData>();
    private ChatListAdapter mAdapter;
    private InputMethodManager imm;
    //当前聊天Id
    private String receiveName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        mMessageListener = this;

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x00001:
                        imm.hideSoftInputFromWindow(
                                contentET.getApplicationWindowToken(), 0); // 隐藏键盘
                        mAdapter.notifyDataSetChanged(); // 刷新聊天列表
                        mListView.setSelection(mListData.size()); // 跳转到listview最底部
                        contentET.setText(""); // 清空发送内容
                        break;

                    default:
                        break;
                }
            }

        };
        receiveName = this.getIntent().getStringExtra("userid");
        initView();
        topNameTV.setText(receiveName);
        emConversation = EMClient.getInstance().chatManager().getConversation(receiveName, null, true);
        //有未读消息才读取会话
        int unreadNum = emConversation.getUnreadMsgCount();
//        LogUtil.i("TAG", "unreadNum=" + unreadNum);
        if (unreadNum != 0) {
            List<EMMessage> allList = emConversation.loadMoreMsgFromDB(emConversation.getLastMessage().getMsgId(), 30);
            allList.add(emConversation.getLastMessage());
            LogUtil.i("TAG", "allList=" + allList.size());
            List<EMMessage> messageList = new ArrayList<>();
            for (int i = allList.size() - unreadNum; i < allList.size(); i++) {
                messageList.add(allList.get(i));
            }
            for (int i = 0; i < messageList.size(); i++) {
                ChatListData data = new ChatListData();
                EMMessageBody tmBody = messageList.get(i).getBody();
                if (!messageList.get(i).getFrom().equals(receiveName)) {
                    data.setType(1);
                    data.setSendContent(((EMTextMessageBody) tmBody).getMessage());
                } else {
                    data.setType(2);
                    data.setReceiveContent(((EMTextMessageBody) tmBody).getMessage());
                }
                mListData.add(data);
            }
        }
        imm = (InputMethodManager) contentET.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        mAdapter = new ChatListAdapter(ChatDetailActivity.this, mListData);
        mListView.setAdapter(mAdapter);
        mListView.setSelection(mListData.size());
        initEvent();
//        refresh();

    }
    //初始化布局
    private void initView() {
        backIV = (ImageView) findViewById(R.id.common_back_iv);
        contentET = (EditText) findViewById(R.id.chat_content);
        topNameTV = (TextView) findViewById(R.id.chat_list_name);
        sendBtn = (Button) findViewById(R.id.chat_send_btn);
        mListView = (ListView) findViewById(R.id.chat_listview);
    }

    private void initEvent() {
        //监听发送按钮
        sendBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 发送
                sendMsg();
            }
        });
        //监听返回按钮
        backIV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // 返回
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("data","refresh");
                LocalBroadcastManager.getInstance(ChatDetailActivity.this).sendBroadcast(intent);
                sendBroadcast(intent);
                finish();
            }
        });

        contentET.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View arg0, int keycode, KeyEvent arg2) {
                // TODO Auto-generated method stub
                if (keycode == KeyEvent.KEYCODE_ENTER
                        && arg2.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMsg();
                    return true;
                }
                return false;
            }
        });
    }

    void sendMessageHX(String username, final String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, username);
        EMClient.getInstance().chatManager().sendMessage(message);
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui

                ChatListData data = new ChatListData();
                data.setSendContent(content);
                data.setType(1);
                mListData.add(data);
                mHandler.sendEmptyMessage(0x00001);
                Log.i("weipeixian", "send message on success");
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui




                Log.i("weipeixian", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调

            }
        });





    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //注册监听
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
        intent.putExtra("data","refresh");
        LocalBroadcastManager.getInstance(ChatDetailActivity.this).sendBroadcast(intent);
        sendBroadcast(intent);
        finish();
    }

    private void sendMsg() {
        String content = contentET.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(getApplicationContext(), "请输入发送的内容",
                    Toast.LENGTH_SHORT).show();
        } else {
            sendMessageHX(receiveName, content);
        }
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
        for (EMMessage message : list) {
            ChatListData data = new ChatListData();

            if (message.getFrom().equals(receiveName)) {
                // 设置消息为已读
                EMMessageBody tmBody = message.getBody();
                emConversation.markMessageAsRead(message.getMsgId());
                // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                data.setReceiveContent(((EMTextMessageBody) tmBody).getMessage());
                data.setType(2);
                mListData.add(data);
                mHandler.sendEmptyMessage(0x00001);
            } else {
                // 如果消息不是当前会话的消息发送通知栏通知
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
