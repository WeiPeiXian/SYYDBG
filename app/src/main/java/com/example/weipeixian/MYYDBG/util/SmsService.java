package com.example.weipeixian.MYYDBG.util;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Process;

/**
 * @author Administrator
 *
 */
public class SmsService extends Service {
    private SmsObserver mObserver;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        ContentResolver resolver = getContentResolver();
        mObserver = new SmsObserver(resolver, new SmsHandler(this));
        resolver.registerContentObserver(Uri.parse("content://sms"), true, mObserver);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.getContentResolver().unregisterContentObserver(mObserver);
        Process.killProcess(Process.myPid());
    }
}
