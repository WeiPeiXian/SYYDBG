package com.example.weipeixian.syydbg;
import android.app.Application;

//import com.easemob.chat.EMChat;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.example.weipeixian.syydbg.ui.activity.MainActivity;

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