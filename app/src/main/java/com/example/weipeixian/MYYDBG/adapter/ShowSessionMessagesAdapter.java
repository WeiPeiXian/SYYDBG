package com.example.weipeixian.MYYDBG.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.MessageHolder;
import com.example.weipeixian.MYYDBG.model.SmsInfo;

import java.util.ArrayList;
import java.util.List;

public class ShowSessionMessagesAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext = null;
    /**
     * 所有的短信
     */
    public static final String SMS_URI_ALL = "content://sms/";

    private static final String GetMessagesByThreadIdTAG = "Getting messages by thread id";

    //存储信息会话中所有来往短信的列表
    List<SmsInfo> infos = new ArrayList<SmsInfo>();


    //ShowSessionMessagesAdapter初始化构造方法
    public ShowSessionMessagesAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    /**
     *根据thread_id 检索sms库， 获得该会话包含的信息
     * @param
     * @return
     */
    public void getSessionMessages(String thread_id){
        Cursor sessionMessagesCursor = null;
        ContentResolver resolver = null;

        /**
         获取短信的各种信息 ，短信数据库sms表结构如下：
         _id：短信序号，如100　　
         　 　 thread_id：对话的序号，如100，与同一个手机号互发的短信，其序号是相同的            　　
         　  　address：发件人地址，即手机号，如+8613811810000            　　
         　  　person：发件人，如果发件人在通讯录中则为具体姓名，陌生人为null            　　
         　  　date：日期，long型，如1256539465022，可以对日期显示格式进行设置            　　
         　  　protocol：协议0SMS_RPOTO短信，1MMS_PROTO彩信            　　
         　  　read：是否阅读0未读，1已读            　　
         　  　status：短信状态-1接收，0complete,64pending,128failed            　　
         　  　type：短信类型1是接收到的，2是已发出            　　
         　  　body：短信具体内容            　　
         　  　service_center：短信服务中心号码编号，如+8613800755500
         */
        try{
            String[] projection = new String[]
                    { "thread_id", "address", "person", "body", "date", "type","read" };


            //Uri uri = Uri.parse("content://sms/");
            Uri uri = Uri.parse(SMS_URI_ALL);
            resolver = mContext.getContentResolver();

            sessionMessagesCursor = resolver.query
                    (
                            uri,
                            projection,
                            "thread_id=?",
                            new String[] { thread_id } ,
                            null
                    );

            if (sessionMessagesCursor == null) {
                return;
            }
            if (sessionMessagesCursor.getCount() == 0){
                sessionMessagesCursor.close();
                sessionMessagesCursor = null;
                return;
            }

            int nameColumn = sessionMessagesCursor.getColumnIndex("person");
            int phoneNumberColumn = sessionMessagesCursor.getColumnIndex("address");
            int smsbodyColumn = sessionMessagesCursor.getColumnIndex("body");
            int dateColumn = sessionMessagesCursor.getColumnIndex("date");
            int typeColumn = sessionMessagesCursor.getColumnIndex("type");

            sessionMessagesCursor.moveToFirst();
            while (sessionMessagesCursor.isAfterLast() == false){
                SmsInfo smsinfo = new SmsInfo();
                //将信息会话的信息内容和信息类型（收到或发出）存入infos中
                smsinfo.setMessage(sessionMessagesCursor.getString(smsbodyColumn));
                smsinfo.setTime(sessionMessagesCursor.getString(dateColumn));
                smsinfo.setType(sessionMessagesCursor.getString(typeColumn));
                infos.add(smsinfo);
                sessionMessagesCursor.moveToNext();
            }

            sessionMessagesCursor.close();
            sessionMessagesCursor = null;
        }catch(Exception e){
            Log.e("cuowu","E:" + e.toString());
        }finally{
            if (sessionMessagesCursor != null){
                sessionMessagesCursor.close();
                sessionMessagesCursor = null;
            }
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder receivedMessageHolder = null;
        MessageHolder sendMessageHolder = null;

        //判断convertView是否已创建，若已存在则不必重新创建新视图，节省系统资源
        if (convertView == null) {

            switch(Integer.parseInt(infos.get(position).getType())){
                //若从sms表提取的信息type为1，说明这是收到的信息
                case 1:
                    //为收到的信息关联格式文件，设置显示格式
                    convertView = mInflater.inflate(R.layout.message_session_list_received_item, null);
                    receivedMessageHolder = new MessageHolder();
                    receivedMessageHolder.setTvTime((TextView)convertView.findViewById(R.id.time));
                    receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(
                            R.id.ReceivedSessionMessageTextView));
                    //将联系人信息载体Holder放入convertView视图
                    convertView.setTag(receivedMessageHolder);
                    break;

                //若从sms表提取的信息type为其他，说明这是发出的信息
                case 2:
                    //为发出的信息关联格式文件，设置显示格式

                    convertView = mInflater.inflate(R.layout.message_session_list_send_item, null);
                    sendMessageHolder = new MessageHolder();
                    receivedMessageHolder.setTvTime((TextView)convertView.findViewById(R.id.time));
                    sendMessageHolder.setTvDesc((TextView) convertView.findViewById(
                            R.id.SendSessionMessageTextView));
                    convertView.setTag(sendMessageHolder);
                    break;
                default:
                    //草稿箱

                    //想想吧
                    break;


            }
        }else{
            //有convertView，按样式，从convertView视图取出联系人信息载体Holder
            switch(Integer.parseInt(infos.get(position).getType()))
            {
                case 1:
                    receivedMessageHolder = (MessageHolder)convertView.getTag();
                    break;
                case 2:
                    sendMessageHolder = (MessageHolder)convertView.getTag();
                    break;
                default:
                    break;
            }
        }

        //设置资源
        switch(Integer.parseInt(infos.get(position).getType()))
        {
            case 1:
                receivedMessageHolder.getTvTime().setText(infos.get(position).getTime());
                receivedMessageHolder.getTvDesc().setText(infos.get(position).getMessage());
                break;
            case 2:
                sendMessageHolder.getTvTime().setText(infos.get(position).getTime());
                sendMessageHolder.getTvDesc().setText(infos.get(position).getMessage());
                break;
            case 3:
                break;
        }

        return convertView;
    }

}
