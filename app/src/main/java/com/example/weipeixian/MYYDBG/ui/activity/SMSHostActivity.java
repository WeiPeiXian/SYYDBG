package com.example.weipeixian.MYYDBG.ui.activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;


import com.example.weipeixian.MYYDBG.AppManager;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.Fragment.FragmentChat;
import com.example.weipeixian.MYYDBG.ui.Fragment.SMSFragment;
import com.example.weipeixian.MYYDBG.util.XmTools;

import butterknife.BindView;

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
//        changeImageSize();
    }

    private void changeImageSize() {
        RadioButton m = (RadioButton)findViewById(R.id.radio_phone);
        RadioButton s = (RadioButton)findViewById(R.id.radio_chat);
        //定义底部标签图片大小
        Drawable drawableFirst = getResources().getDrawable(R.drawable.host_phone_rb);
        drawableFirst.setBounds(0, 0, 69, 69);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        m.setCompoundDrawables(null, drawableFirst, null, null);//只放上面
        Drawable drawableMe = getResources().getDrawable(R.drawable.host_message_rb);
        drawableMe.setBounds(0, 0, 69, 69);//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        s.setCompoundDrawables(null, drawableMe, null, null);//只放上面
    }
}