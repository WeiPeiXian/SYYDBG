package com.example.weipeixian.syydbg.ui.Fragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSFragment {
//    private Uri SMS_INBOX = Uri.parse("content://sms/");
//    public void getSmsFromPhone() {
//        ContentResolver cr = getContentResolver();
//        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
//        String where = " address = '1066321332' AND date >  "
//                + (System.currentTimeMillis() - 10 * 60 * 1000);
//        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
//        if (null == cur)
//            return;
//        if (cur.moveToNext()) {
//            String number = cur.getString(cur.getColumnIndex("address"));//手机号
//            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
//            String body = cur.getString(cur.getColumnIndex("body"));
//            //这里我是要获取自己短信服务号码中的验证码~~
//            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
//            Matcher matcher = pattern.matcher(body);
//            if (matcher.find()) {
//                String res = matcher.group().substring(1, 11);
//                mobileText.setText(res);
//            }
//        }
//    }

}
