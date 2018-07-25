package com.example.weipeixian.syydbg.ui.activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.TabHost;


import com.example.weipeixian.syydbg.AppManager;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.ui.Fragment.FragmentChat;
import com.example.weipeixian.syydbg.util.XmTools;

import cn.leancloud.chatkit.activity.LCIMConversationListFragment;

/**
 * Date: 2016/8/31 16:38
 * Email: diyangxia@163.com
 * Author: diyangxia
 * Description: TODO
 */
public class SMSHostActivity extends FragmentActivity {
    private FragmentTabHost mFragmentTabhost;
    public static final String SHOW_OF_CHAT_TAG = "chat";
    public static final String SHOW_OF_PHONE_TAG = "message";
    RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_tab);
        AppManager.getInstance().addActivity(this);
        mFragmentTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        radioGroup = (RadioGroup) findViewById(R.id.main_radio);
        mFragmentTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);


        TabHost.TabSpec tabSpec0 = mFragmentTabhost.newTabSpec(SHOW_OF_PHONE_TAG)
                .setIndicator("0");
        TabHost.TabSpec tabSpec1 = mFragmentTabhost.newTabSpec(SHOW_OF_CHAT_TAG)
                .setIndicator("1");
        mFragmentTabhost.addTab(tabSpec0, LCIMConversationListFragment.class, null);
        //内部短信
        mFragmentTabhost.addTab(tabSpec1, FragmentChat.class, null);
        mFragmentTabhost.setCurrentTabByTag(SHOW_OF_PHONE_TAG);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_chat) {
                    mFragmentTabhost.setCurrentTabByTag(SHOW_OF_CHAT_TAG);
                } else if (checkedId == R.id.radio_phone) {
                    mFragmentTabhost.setCurrentTabByTag(SHOW_OF_PHONE_TAG);
                }
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            XmTools.showExitDialog(SMSHostActivity.this);
        }
        return super.onKeyDown(keyCode, event);
    }


}