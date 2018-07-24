package com.example.weipeixian.syydbg;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;

//import com.easemob.chat.EMChat;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.example.weipeixian.syydbg.ui.ChatDetailActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

public class BaseApplication extends Application {
    private final String APP_ID = "i4YKTRWxDNATl5uWByPWJtUp-gzGzoHsz";
    private final String APP_KEY = "QCO2a3xBXaFtWSJr0ND0O1hB";
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication mInstance = null;
    private boolean isInit = false;
    @Override
    public void onCreate() {
        super.onCreate();
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        AVOSCloud.setDebugLogEnabled(true);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        AVIMClient.setAutoOpen(true);
        PushService.setDefaultPushCallback(this, MainActivity.class);
        PushService.setAutoWakeUp(true);
        PushService.setDefaultChannelId(this, "default");

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 保存成功
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println("---  " + installationId);
                } else {
                    // 保存失败，输出错误信息
                    System.out.println("failed to save installation.");
                }
            }
        });
    }


    public static BaseApplication getInstance() {
        return mInstance;
    }

}