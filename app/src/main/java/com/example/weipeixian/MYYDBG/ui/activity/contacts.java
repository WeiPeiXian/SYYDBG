package com.example.weipeixian.MYYDBG.ui.activity;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.PermissionListener;
import com.example.weipeixian.MYYDBG.model.SmsInfo;

import java.util.List;

public class contacts extends BaseActivity{
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle saveInstancestate) {
        super.onCreate(saveInstancestate);
        requestPermission();
    }
    private void requestPermission(){
        requestRunTimePermission(new String[]{Manifest.permission.READ_CONTACTS}
                , new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onGranted() {  //所有权限授权成功
                        get();
                    }
                    @Override
                    public void onGranted(List<String> grantedPermission) { //授权失败权限集合
                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) { //授权成功权限集合
                    }
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void get(){
        Cursor mCursor = null;
        try{
            mCursor = getContentResolver().query(Uri.parse("content://sms/"), new String[] { "_id", "address", "read", "body", "thread_id" }, "read=?", new String[] { "0" }, "date desc");
            int nameColumn = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);//姓名
            Log.d("null", String.valueOf(mCursor));
            Log.d("SSSSSSSSSSSSSSSSSSSSSS",String.valueOf(mCursor));
            Log.d("SSSSSSSSSSSSSSSSSSSSSS",String.valueOf(nameColumn));

            if (mCursor.moveToFirst()) {
                Log.d("null", String.valueOf(mCursor));
                Log.d("null", String.valueOf(nameColumn));
                do {
                    int _inIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    if (_inIndex != -1) {
                        String m = mCursor.getString(_inIndex);
                        Log.d("null", m);
                    }
                } while (mCursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCursor !=null){
                mCursor.close();
                Log.d("SSSSSSSSSSSSSSSSSSSSSS","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            }
        }
    }
}
