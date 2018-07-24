package com.example.weipeixian.syydbg;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.util.Log;

//import com.easemob.chat.EMChat;
import com.avos.avoscloud.AVOSCloud;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.example.weipeixian.syydbg.ui.ChatDetailActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMOptions;

import java.util.Iterator;
import java.util.List;

public class BaseApplication extends Application {
    private static final String TAG = BaseApplication.class.getSimpleName();
    private static BaseApplication mInstance = null;
    private Context mContext;
    // 记录是否已经初始化
    private boolean isInit = false;
    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mContext = this;
        // 初始化环信SDK3.x
        initEasemob();
        mInstance = this;
        AVOSCloud.initialize(this,"i4YKTRWxDNATl5uWByPWJtUp-gzGzoHsz","QCO2a3xBXaFtWSJr0ND0O1hB");
        AVOSCloud.setDebugLogEnabled(true);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    private void initEasemob() {
        // 获取当前进程 id 并取得进程名
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        /**
         * 如果app启用了远程的service，此application:onCreate会被调用2次
         * 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
         * 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process name就立即返回
         */
        if (processAppName == null || !processAppName.equalsIgnoreCase(mContext.getPackageName())) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        /**
         * SDK初始化的一些配置
         * 关于 EMOptions 可以参考官方的 API 文档
         * http://www.easemob.com/apidoc/ ... .html
         */
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，TODO 这个暂时有bug，上层收不到发送回执
        options.setRequireDeliveryAck(true);
        EMClient.getInstance().init(mContext, options);
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);
        // 设置初始化已经完成
        isInit = true;
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
//     * @param pid 进程的id
     * @return 返回进程的名字
     */


    /*
     * (non-Javadoc)
     *
     * @see android.app.Application#onCreate()
     */




    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this
                .getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
                    .next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm
                            .getApplicationInfo(info.processName,
                                    PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    @Override
    public void onLowMemory() {
        // TODO Auto-generated method stub
        super.onLowMemory();
        Log.i(TAG, "onLowMemory");
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        Log.i(TAG, "onTerminate");
        super.onTerminate();
    }

    public static BaseApplication getInstance() {
        return mInstance;
    }

}