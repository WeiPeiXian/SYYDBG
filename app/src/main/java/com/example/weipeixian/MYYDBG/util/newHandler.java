package com.example.weipeixian.MYYDBG.util;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.model.SmsInfo;

import java.util.List;

/**
 * @author 短信的处理
 *
 */
public 	class newHandler extends Handler
{
    private Context  mcontext;

    public newHandler(Context  context)
    {
        this.mcontext=context;
    }

    @Override
    public void handleMessage(Message msg)
    {
        //增加处理
//        SmsInfo smsInfo=(SmsInfo)msg.obj;
//        if(smsInfo.action==1) {
//            ContentValues values = new ContentValues();
//            values.put("read", "1");
//            mcontext.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "thread_id=?", new String[]{smsInfo.thread_id});
//        }
//        else if(smsInfo.action==2) {
//            Uri mUri=Uri.parse("content://sms/");
//            mcontext.getContentResolver().delete(mUri, "_id=?", new String[]{smsInfo._id});
//        }
    }
}
