package com.example.weipeixian.MYYDBG.ui.Fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.provider.Telephony;
import android.provider.Telephony.Threads;
import android.renderscript.Allocation;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.LogUtil;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.SmsListAdapter;
import com.example.weipeixian.MYYDBG.model.ContactData;
import com.example.weipeixian.MYYDBG.model.ContactItem;
import com.example.weipeixian.MYYDBG.model.SmsInfo;
import com.example.weipeixian.MYYDBG.ui.activity.ShowSessionMessagesActivity;
import com.example.weipeixian.MYYDBG.ui.activity.send_sms;
import com.example.weipeixian.MYYDBG.util.SmsHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import static android.widget.AdapterView.*;
import static com.example.weipeixian.MYYDBG.util.LogUtil.ERROR;
import static java.util.Calendar.DATE;

public class SMSFragment extends BaseFragment{
    private static ArrayList<Threads> list;
    final String SMS_URI_ALL = "content://sms/";         //所有短信
    final String SMS_URI_INBOX = "content://sms/inbox";    //收信箱
    final String SMS_URI_SEND = "content://sms/sent";    //发信箱
    final String SMS_URI_DRAFT = "content://sms/draft";    //草稿箱
    private static final String MesTAG = "Reading Messages";
    private static final String ThreadTAG = "Reading from Thread Table";
    private static final String GetPhoneNumberTAG = "Getting phone number";
    private static final String GetContactByPhoneTAG = "Getting contact by phone number";
    private static Threads threads;
    RecyclerView recyclerView;
    private SmsObserver smsObserver;
    private Uri SMS_INBOX = Uri.parse("content://sms/");
    private ImageButton send;
    private Toolbar toolbar;
    private final int BACK_INTERVAL = 1000;
    public static EditText ed;
    List<SmsInfo> smslist = new ArrayList<>();
    SmsListAdapter adapter;
    private static TextView back;
    private View view;
    public static Context tcontext;
    private Boolean ShowKeyboard= false;


    @SuppressLint("HandlerLeak")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phone_message, container, false);
        send = (ImageButton) view.findViewById(R.id.new_phone_message);
        LinearLayout layout = view.findViewById(R.id.layout);
        ed = view.findViewById(R.id.research);
        back = view.findViewById(R.id.back);
        tcontext =getActivity();
        ed.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                back.setVisibility(View.VISIBLE);
                back.setClickable(true);
                return false;
            }
        });
        ed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                back.setClickable(false);
            }
        });
        view.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        cancle(view,getActivity(),ed);
        cancle(back,getActivity(),ed);
        cancle(layout,getActivity(),ed);
        send.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),send_sms.class));
                Toast.makeText(getActivity(),"进入发送界面",Toast.LENGTH_SHORT).show();
            }
        });
        mHandler =new SmsHandler(mContext){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                adapter = new SmsListAdapter(mContext, smslist);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
//                        通过Intent向显示短信会话包含的信息的Activity传递会话id
                        intent.putExtra("threadId",smslist.get(position).getThread_id());
                        intent.setClass(mContext, ShowSessionMessagesActivity.class);
                        getActivity().startActivity(intent);
                    }
                });
                adapter.setOnItemLongClickListener(new OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        Toast.makeText(getActivity(),"这里是一个长点击事件"+position,Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.sms_recycleView);
        cancle(recyclerView,getActivity(),ed);
        initRecyclerview();
        smsObserver = new SmsObserver(getActivity(),mHandler);
        getActivity().getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);
        getMessageSessions();
        return view;

    }
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，包括标题栏不包括状态栏
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            // 键盘最小高度
            int minKeyboardHeight = 150;
            // 屏幕高度,不含虚拟按键的高度
            int screenHeight = view.getRootView().getHeight();
            // 获取状态栏高度
            int statusBarHeight = r.top;
            // 在不显示软键盘时，height等于状态栏的高度
            int height = screenHeight - (r.bottom - r.top);
            //Log.i("wyy", "onGlobalLayout: height = " + height + ", staheight  =" + statusBarHeight + ", r.top = " + r.top + ", r.bottom = " + r.bottom);
            if (ShowKeyboard) {
                // 如果软键盘是弹出的状态，并且height小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (height - statusBarHeight < minKeyboardHeight) {
                    //ToastUtils.showToast(GoodsAllStockActivity.this, "键盘隐藏了");
                    back.setVisibility(View.GONE);
                    back.setClickable(false);
                    ed.setText("");
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                    ShowKeyboard = false;
                }
            } else {
                if (height - statusBarHeight > minKeyboardHeight) {
                    ShowKeyboard = true;
                }
            }
        }
    };


    //取消聚焦
    public static void cancle(final View loutSoft, final Context context, final EditText editText){
        loutSoft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loutSoft.setFocusable(true);
                loutSoft.setFocusableInTouchMode(true);
                loutSoft.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                ed.setText("");
                back.setVisibility(View.GONE);
                back.setClickable(false);
                return false;
            }
        });
    }

    //初始化列表
    private void initRecyclerview(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
    }

    //初始化
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    //观测
    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getMessageSessions();
        }
    }

    @SuppressLint("LongLogTag")
    //获取会话列表
    public void getMessageSessions(){
        smslist.clear();
        //会话列表Cusor
        Cursor sessionCursor = null;
        ContentResolver resolver = null;
        ContactData contact = null;
        try{
            Uri uri = Uri.parse(SMS_URI_ALL);
            resolver = getActivity().getContentResolver();
            sessionCursor = resolver.query(uri, new String[]
                    { "* from threads--" }, null, null, null);
            if (sessionCursor == null) {
                Log.d("会话","获取Cusor 失败");
                return;
            }
            if (sessionCursor.getCount() == 0){
                Log.d("会话","会话列表为空");
                sessionCursor.close();
                sessionCursor = null;
                return;
            }
            else {
                sessionCursor.moveToFirst();
                while (sessionCursor.isAfterLast() == false)
                {  /*
                threads表各字段含义如下：
                2.recipient_ids为联系人ID,这个ID不是联系人表中的_id,而是指向表canonical_address里的id,
                canonical_address这个表同样位于mmssms.db,它映射了recipient_ids到一个电话号码,也就是说,
                最终获取联系人信息,还是得通过电话号码;
                3.mesage_count该会话的消息数量
                4.snippet为最后收到或发出的信息
                */
                    //会话id
                    int thread_idColumn = sessionCursor.getColumnIndex("_id");
                    int dateColumn = sessionCursor.getColumnIndex("date");
                    int message_countColumn = sessionCursor.getColumnIndex("message_count");
                    int snippetColumn = sessionCursor.getColumnIndex("snippet");
                    int typeColumn = sessionCursor.getColumnIndex("type");
                    int x = sessionCursor.getColumnIndex("recipient_ids");
                    //格式化短信日期显示
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date(Long.parseLong(sessionCursor.getString(dateColumn)));
                    //String time = dateFormat.format(date);
                    /*
                     *  获取短信的各项内容
                     *  phoneAndUnread[0]存放电话号码，phoneAndUnread[1]存放该会话中未读信息数*/
                    //根据短信会话的threadId检索sms表，获取该短信会话人号码
                    String threadid = sessionCursor.getString(thread_idColumn);
                    String phoneAndUnread[]=getPhoneNum(sessionCursor.getString(thread_idColumn));
                    String last_mms=sessionCursor.getString(snippetColumn);
                    String date_mms=dateFormat.format(date);
                    String count_mms=sessionCursor.getString(message_countColumn);
                    String type = sessionCursor.getString(typeColumn);
                    SmsInfo smsinfo = new SmsInfo();
                    smsinfo.setThread_id(threadid);
                    /*
                     * phoneAndUnread[0]存放电话号码
                     * 根据短信会话人号码查询手机联系人，获取会话人的名称和头像资料
                     */
                    contact = getContactFromPhoneNum(mContext, phoneAndUnread[0]);
                    //获得最后的未读短信与已读短信
//                    String final_count="("+phoneAndUnread[1]+"/"+count_mms+")";
                    //将会话信息添加到信息列表中
                    //判断是否联系人，若为联系人显示其名称，若不是则显示号码
                    if (contact.getContactName().equals("")){
                        smsinfo.setContactMes(phoneAndUnread[0]);
                    }else{
                        smsinfo.setContactMes(contact.getContactName());
                    }
                    if (smsinfo.getContactMes()==null){
                        int threadId = sessionCursor.getInt(x);
                        Cursor cursorCanonicalAddress = resolver.query(
                                Uri.parse("content://mms-sms/canonical-address/" + threadId), null, null, null, null);
                        if (cursorCanonicalAddress.moveToFirst()) {
                            String name = cursorCanonicalAddress.getString(cursorCanonicalAddress.getColumnIndex("address"));
                            smsinfo.setContactMes(name);
                        }
                        smsinfo.setMessage("<font color='#ff0000'>[草稿]</font>"+last_mms);
                        smsinfo.setDate(date_mms);
                        smsinfo.setType(type);
                        smsinfo.setMessageCout(count_mms);
                        cursorCanonicalAddress.close();
                    }
                    else {
                        smsinfo.setDate(date_mms);
                        System.out.println(type+"="+last_mms);
                        if (type.equals("3")){
                            smsinfo.setMessage("<font color='#ff0000'>[草稿]</font>"+last_mms);
                        }
                        else
                            smsinfo.setMessage(last_mms);
                        smsinfo.setType(type);
                        smsinfo.setMessageCout(count_mms);
                    }
                    smslist.add(smsinfo);

                    sessionCursor.moveToNext();

                }
                sessionCursor.close();
            }

        }catch(Exception e){
            Log.e(ThreadTAG,"E:" + e.toString());
        }finally{
            if (sessionCursor != null){
                sessionCursor.close();
                sessionCursor = null;
            }
        }
        Collections.reverse(smslist);
        SmsInfo m = new SmsInfo();
        Message msg=mHandler.obtainMessage();
        msg.obj=m;
        mHandler.sendMessage(msg);

    }
    /**
     * 根据thread_id 检索sms库， 获得对应的号码以及该号码的未读信息数
     * @param
     * @return
     */
    public String[] getPhoneNum(String thread_id){
        Cursor cursor = null;
        String PhoneNum="";
        int noread_mms=0;
        String[] phoneAndUnread={"",""};
        try{
            String[] projection = new String[]
                    { "thread_id", "address", "person", "body", "date", "type","read" };

            //SMS_URI_ALL = "content://sms/";
            Uri uri = Uri.parse(SMS_URI_ALL);
            ContentResolver resolver = mContext.getContentResolver();
            cursor = resolver.query(uri, projection, "thread_id=?", new String[] { thread_id } , null);
            //计算该会话包含的未读短信数


            while (cursor.moveToNext()){
                int phoneNumber = cursor.getColumnIndex("address");
                int isread =cursor.getColumnIndex("read");
                //sms表的read字段为0，表示该短信为未读短信
                if (cursor.getString(isread).equals("0"))
                {
                    noread_mms++;
                }
                PhoneNum=cursor.getString(phoneNumber);
            }
            phoneAndUnread[0]=PhoneNum;
            phoneAndUnread[1]=Integer.toString(noread_mms);
            cursor.close();
            cursor = null;
        }catch(Exception e){
            Log.e(GetPhoneNumberTAG,"E:" + e.toString());
        }finally{
            if (cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return phoneAndUnread;
    }


    //根据联系人号码从通讯录中获取联系人信息，包括名称和头像uri
    @SuppressLint("LongLogTag")
    public ContactData getContactFromPhoneNum(Context context, String phoneNum)
    {
        String phone = phoneNum;
        ContactData contact = new ContactData();
        contact.setContactName("");
        ContentResolver resolver = null;
        Cursor cursor = null;
        String contactName;
        Long photoId;
        Long contactId;

        try{
            resolver = mContext.getContentResolver();

            //根据电话号码号码查询联系人数据库，获取对应的联系人资料
            cursor = resolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,  ContactsContract.CommonDataKinds.Phone.NUMBER + " = ?",
                    new String[]{phone}, null);

            //如果查询结果为空，说明该短信会话人并非联系人，直接返回空联系人对象
            if(cursor == null){
                return contact;
            }
            if(cursor.getCount() == 0){
                cursor.close();
                cursor = null;
                return contact;
            }

            //若查询成功，说明该短信会话人是手机联系人，返回联系人号码和头像资料
            if (cursor.moveToFirst()){
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.
                        CommonDataKinds.Phone.DISPLAY_NAME));

                contactId = (Long)cursor.getLong(cursor.getColumnIndex(ContactsContract.
                        CommonDataKinds.Phone.CONTACT_ID));



                contact.setContactName(contactName);
                cursor.close();
                cursor = null;
                return contact;
            }

        }catch(Exception e){
            Log.e(GetContactByPhoneTAG,"E:" + e.toString());
        }finally{
            if (cursor != null){
                cursor.close();
                cursor = null;
            }
        }
        return contact;
    }

}
