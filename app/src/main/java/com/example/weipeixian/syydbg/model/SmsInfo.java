package com.example.weipeixian.syydbg.model;

/**
 * 主要用于短信拦截
 * @author Administrator
 *
 */
public class SmsInfo {
    public String _id="";
    public String thread_id = "";
    public String smsAddress = "";
    public String smsBody = "";
    public String read="";
    public int action=0;//1代表设置为已读，2表示删除短信
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
        return read;
    }
    public void setTime(String time) {
        this._id = time;
    }
}
