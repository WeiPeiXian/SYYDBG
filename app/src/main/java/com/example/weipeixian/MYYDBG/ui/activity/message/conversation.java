package com.example.weipeixian.MYYDBG.ui.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTemporaryConversation;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.example.weipeixian.MYYDBG.R;

import java.util.Arrays;

import cn.leancloud.chatkit.LCChatKit;
import cn.leancloud.chatkit.activity.LCIMConversationFragment;
import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.utils.LCIMConstants;
import com.example.weipeixian.MYYDBG.util.LCIMConversationUtils;
import cn.leancloud.chatkit.utils.LCIMLogUtils;

public class conversation extends AppCompatActivity {
    protected LCIMConversationFragment conversationFragment;
    private TextView tx;
    private  AVIMTextMessage im;
    private ImageButton back;
    String memberid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        tx = (TextView) findViewById(R.id.textView);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        conversationFragment = (LCIMConversationFragment)getSupportFragmentManager().findFragmentById(cn.leancloud.chatkit.R.id.fragment_chat);
        initByIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        if (null == LCChatKit.getInstance().getClient()) {
            showToast("please login first!");
            finish();
            return;
        }

        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(LCIMConstants.PEER_ID)) {
                memberid = extras.getString(LCIMConstants.PEER_ID);
                getConversation(extras.getString(LCIMConstants.PEER_ID));
                String content =  extras.getString("content");
                im = new AVIMTextMessage();
                im.setText(content);
                tx.setText(extras.getString("jieshou"));
                //这个再看看已有会话
            } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
                //获取已知会话，更新会话
                String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
                String name = extras.getString("name");
                tx.setText(extras.getString("name"));
                updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId));
            } else {
                showToast("memberId or conversationId is needed");
                finish();
            }
        }
    }
    /**
     * 设置 actionBar title 以及 up 按钮事件
     *
     * @param title
     */
    protected void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            if (null != title) {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            finishActivity(RESULT_OK);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 主动刷新 UI
     *
     * @param conversation
     */
    protected void updateConversation(AVIMConversation conversation) {
        if (null != conversation) {
            if (conversation instanceof AVIMTemporaryConversation) {
                System.out.println("Conversation expired flag: " + ((AVIMTemporaryConversation)conversation).isExpired());
            }
            conversationFragment.setConversation(conversation);


            LCIMConversationItemCache.getInstance().insertConversation(conversation.getConversationId());
                initActionBar(LCIMConversationUtils.getConversationName(conversation));

        }
    }

    /**
     * 获取 conversation
     * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
     */
    protected void getConversation(final String memberId) {
        LCChatKit.getInstance().getClient().createConversation(
                Arrays.asList(memberId), "", null, false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (null != e) {
                            showToast(e.getMessage());
                        } else {
                            if (im ==null){
                                updateConversation(avimConversation);
                            }
                            while (im!=null){
                                avimConversation.sendMessage(im,  new AVIMConversationCallback() {
                                    @Override
                                    public void done(AVIMException e) {
                                        if (null != e) {
                                            LCIMLogUtils.logException(e);
                                        }
                                    }
                                });
                                getConversation(memberId);
                                im = null;
                            }

                        }
                    }
                });
    }
    /**
     * 弹出 toast
     *
     * @param content
     */
    private void showToast(String content) {
        Toast.makeText(conversation.this, content, Toast.LENGTH_SHORT).show();
    }
}
