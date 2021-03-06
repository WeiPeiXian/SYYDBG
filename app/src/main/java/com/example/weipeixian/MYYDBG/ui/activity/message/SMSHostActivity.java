package com.example.weipeixian.MYYDBG.ui.activity.message;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.widget.RadioGroup;
import android.widget.TabHost;


import com.example.weipeixian.MYYDBG.util.AppManager;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.Fragment.message.FragmentChat;
import com.example.weipeixian.MYYDBG.ui.Fragment.message.SMSFragment;


public class SMSHostActivity extends FragmentActivity {
    private FragmentTabHost mFragmentTabhost;
    public static final String SHOW_OF_CHAT_TAG = "chat";
    public static final String SHOW_OF_PHONE_TAG = "message";
    RadioGroup radioGroup;
    Toolbar toolbar;
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
        mFragmentTabhost.addTab(tabSpec0, SMSFragment.class, null);
        //message
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

}