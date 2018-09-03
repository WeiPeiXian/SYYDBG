package com.example.weipeixian.MYYDBG.model;

import android.graphics.Bitmap;

/**
 * 主要用于短信拦截
 * @author Administrator
 *
 */
public class SmsInfo {
    public String _id="";
    public String thread_id = "";
    public String name;
    public String smsAddress = "";
    public String smsBody = "";
    private String date;
    public String read="";
    public int action=0;//1代表设置为已读，2表示删除短信
    /**
     * 短信类型1是接收到的，通过SQLiteExpertPers查看发出信息类型为6
     */
    private String type;
    /**
     * 与同一个联系人的会话包含的消息数
     */
    private String messageCout;
    /**
     * 会话人的信息，属于联系人则为名称，否则为其号码
     */
    private String contactMes;
    private String phone;
    public String getPhone() {
        return phone;
    }
    public void setPhone(String thread_id) {
        this.phone = thread_id;
    }


    public String getThread_id() {
        return thread_id;
    }
    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }



    public String getContactMes() {
        return contactMes;
    }
    public void setContactMes(String contactMes) {
        this.contactMes = contactMes;
    }
    public String getMessageCout() {
        return messageCout;
    }
    public void setMessageCout(String messageCout) {
        this.messageCout = messageCout;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getUsername() {
        return smsAddress;
    }
    public void setUsername(String username) {
        this.smsAddress = username;
    }
    public String getMessage() {
        return smsBody;
    }
    public void setMessage(String message) {
        this.smsBody = message;
    }
    public String getTime() {
        return date;
    }
    public void setTime(String time) {
        this.date = time;
    }
}
