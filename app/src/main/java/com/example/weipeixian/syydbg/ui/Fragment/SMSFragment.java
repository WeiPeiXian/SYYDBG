package com.example.weipeixian.syydbg.ui.Fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.avos.avoscloud.LogUtil;
import com.example.weipeixian.syydbg.BaseFragment;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.adapter.SmsListAdapter;
import com.example.weipeixian.syydbg.model.ContactItem;
import com.example.weipeixian.syydbg.model.SmsInfo;
import com.example.weipeixian.syydbg.util.SmsHandler;

import java.util.ArrayList;
import java.util.List;

public class SMSFragment extends BaseFragment{
    RecyclerView recyclerView;
    private SmsObserver smsObserver;
    public SmsHandler smsHandler;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private Button send;
    private Toolbar toolbar;
    private final int BACK_INTERVAL = 1000;
    List<SmsInfo> smslist = new ArrayList<>();

    SmsListAdapter adapter;
    @SuppressLint("HandlerLeak")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms, container, false);
        send = (Button) view.findViewById(R.id.send);
        recyclerView = (RecyclerView) view.findViewById(R.id.sms_recycleView);
        initRecyclerview();
        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
//                跳转到发送信息页面
//                startActivity(new Intent(mContext, send.class));
            }
        });
        smsHandler =new SmsHandler(mContext){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                adapter = new SmsListAdapter(mContext, smslist);
                recyclerView.setAdapter(adapter);
            }
        };
        mHandler = smsHandler;
        smsObserver = new SmsObserver(getActivity(),mHandler);
        getActivity().getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);
        getSmsFromPhone();
        return view;
    }
    private void initRecyclerview(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void getSmsFromPhone() {
        smslist.clear();
        final String SMS_URI_ALL = "content://sms/";         //所有短信
        final String SMS_URI_INBOX = "content://sms/inbox";    //收信箱
        final String SMS_URI_SEND = "content://sms/sent";    //发信箱
        final String SMS_URI_DRAFT = "content://sms/draft";    //草稿箱
        ContentResolver mResolver = getActivity().getContentResolver();
        Cursor mCursor=mResolver.query(Uri.parse(SMS_URI_ALL), new String[] { "* from threads--" }, "read=?", new String[] { "0" }, "date desc");
        if(mCursor==null) {
            Log.d("null","meg");
            return ;
        } else {
            int nameColumn = mCursor.getColumnIndex("_id");//姓名
            Log.d("null", String.valueOf(mCursor));
            Log.d("null", String.valueOf(nameColumn));
            if (mCursor.moveToFirst()) {
//                int nameColumn = mCursor.getColumnIndex("_id");//姓名
                Log.d("null", String.valueOf(mCursor));
                Log.d("null", String.valueOf(nameColumn));
                do {
                    try{
                    SmsInfo _smsInfo = new SmsInfo();
                    int _inIndex = mCursor.getColumnIndex("_id");
                    if (_inIndex != -1) {
                        _smsInfo._id = mCursor.getString(_inIndex);
                        Log.d("null", _smsInfo._id);
                    }
                    int thread_idIndex = mCursor.getColumnIndex("thread_id");
                    if (thread_idIndex != -1) {
                        _smsInfo.thread_id = mCursor.getString(thread_idIndex);
                    }
                    int addressIndex = mCursor.getColumnIndex("address");
                    if (addressIndex != -1) {
                        _smsInfo.smsAddress = mCursor.getString(addressIndex);
                        Uri personUri = Uri.withAppendedPath(
                                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, _smsInfo.smsAddress);
                        Cursor cur = getActivity().getContentResolver().query(personUri,
                                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME },
                                null, null, null );
                        if( cur.moveToFirst() ) {
                            int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                            ContactItem item = new ContactItem();
                            String name = cur.getString(nameIdx);
                            _smsInfo.setUsername(name);
//            return item;
                        }
                    }
                    int bodyIndex = mCursor.getColumnIndex("body");
                    if (bodyIndex != -1) {
                        _smsInfo.smsBody = mCursor.getString(bodyIndex);
                    }
                    int readIndex = mCursor.getColumnIndex("read");
                    if (readIndex != -1) {
                        _smsInfo.read = mCursor.getString(readIndex);
                    }//TODO
                    Log.d("smslist.toString", _smsInfo._id + "///" + _smsInfo.thread_id + "///" + _smsInfo.smsAddress + "///" + _smsInfo.smsBody + "///" + _smsInfo.read);
                    smslist.add(_smsInfo);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                } while (mCursor.moveToNext());
            }
            else {
                LogUtil.log.i("不行哇，这个有毛病 ，movetofirst 失败怎么办");
            }
        }
        if(mCursor!=null) {
            mCursor.close();
            mCursor=null;
            Log.d("mCursor","meg");

        }
        SmsInfo m = new SmsInfo();
        Message msg=mHandler.obtainMessage();
        msg.obj=m;
        mHandler.sendMessage(msg);

    }
    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSmsFromPhone();
        }
    }

    void getContactByAddr(Context context, final SmsInfo sms) {
        Uri personUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI, sms.smsAddress);
        Cursor cur = context.getContentResolver().query(personUri,
                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME },
                null, null, null );
        if( cur.moveToFirst() ) {
            int nameIdx = cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
            ContactItem item = new ContactItem();
            String name = cur.getString(nameIdx);
            cur.close();
//            return item;
        }
//        return null;
    }
}
