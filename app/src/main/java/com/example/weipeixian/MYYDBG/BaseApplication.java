package com.example.weipeixian.MYYDBG;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

//import com.easemob.chat.EMChat;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.example.weipeixian.MYYDBG.ui.activity.open_page;

import cn.leancloud.chatkit.LCChatKit;

public class BaseApplication extends MultiDexApplication {
    private final String APP_ID = "i4YKTRWxDNATl5uWByPWJtUp-gzGzoHsz";
    private final String APP_KEY = "QCO2a3xBXaFtWSJr0ND0O1hB";
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication mInstance = null;
    private boolean isInit = false;
    private static Context context;

    //返回
    public static Context getContext(){
        return context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AVOSCloud.setDebugLogEnabled(true);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        PushService.setDefaultPushCallback(this, open_page.class);
        PushService.setAutoWakeUp(true);
        PushService.setDefaultChannelId(this, "default");
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                    System.out.println("---  " + installationId);
                } else {
                    System.out.println("failed to save installation.");
                }
            }
        });

    }


    public static BaseApplication getInstance() {
        return mInstance;
    }

}