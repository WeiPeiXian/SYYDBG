package com.example.weipeixian.MYYDBG.adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.holder.MessageHolder;
import com.example.weipeixian.MYYDBG.model.SmsInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ShowSessionMessagesAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext = null;
    public static final String SMS_URI_ALL = "content://sms/";
    private static final String GetMessagesByThreadIdTAG = "Getting messages by thread id";
    List<SmsInfo> infos = new ArrayList<SmsInfo>();
    public ShowSessionMessagesAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }
    public void getSessionMessages(String thread_id){
        Cursor sessionMessagesCursor = null;
        ContentResolver resolver = null;
        try{
            String[] projection = new String[]{ "thread_id", "address", "person", "body", "date", "type","read" };
            Uri uri = Uri.parse(SMS_URI_ALL);
            resolver = mContext.getContentResolver();
            sessionMessagesCursor = resolver.query(uri, projection, "thread_id=?", new String[] { thread_id } ,"date DESC");
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
            SimpleDateFormat format =  new SimpleDateFormat( "MM-dd HH:mm:ss" );
            sessionMessagesCursor.moveToFirst();
            while (sessionMessagesCursor.isAfterLast() == false){
                SmsInfo smsinfo = new SmsInfo();
                //将信息会话的信息内容和信息类型（收到或发出）存入infos中
                String date = null;
                long m = Long.parseLong(sessionMessagesCursor.getString(dateColumn));
                try {
                    date = format.format(m);
                }catch (Exception e){
                    e.printStackTrace();
                }
                smsinfo.setMessage(sessionMessagesCursor.getString(smsbodyColumn));
                smsinfo.setTime(date);
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
        return infos.size();
    }
    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageHolder receivedMessageHolder = null;
        MessageHolder sendMessageHolder = null;
        if (convertView == null) {
            switch(Integer.parseInt(infos.get(position).getType())){
                case 1:
                    convertView = mInflater.inflate(R.layout.message_session_list_received_item, null);
                    receivedMessageHolder = new MessageHolder();
                    receivedMessageHolder.setTvTime((TextView)convertView.findViewById(R.id.time));
                    receivedMessageHolder.setTvDesc((TextView) convertView.findViewById(R.id.ReceivedSessionMessageTextView));
                    convertView.setTag(receivedMessageHolder);
                    break;
                    case 2:
                    convertView = mInflater.inflate(R.layout.message_session_list_send_item, null);
                    sendMessageHolder = new MessageHolder();
                    sendMessageHolder.setTvTime((TextView)convertView.findViewById(R.id.time));
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
        switch(Integer.parseInt(infos.get(position).getType())){
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
        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.message_session_list_send_item, null);
            sendMessageHolder = new MessageHolder();
            sendMessageHolder.setTvDesc((TextView) convertView.findViewById(R.id.SendSessionMessageTextView));
            sendMessageHolder.setTvTime((TextView) convertView.findViewById(R.id.time));
            sendMessageHolder.getTvTime().setVisibility(View.GONE);
            sendMessageHolder.getTvDesc().setVisibility(View.GONE);
        }
        return convertView;
    }

}
