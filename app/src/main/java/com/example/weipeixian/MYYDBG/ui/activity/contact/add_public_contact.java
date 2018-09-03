package com.example.weipeixian.MYYDBG.ui.activity.contact;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.R;

import java.text.SimpleDateFormat;

public class add_public_contact extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setContentView(R.layout.change_phone);
        setTouchListener(this);//只需调用这个方法即可
        Button btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat ("yy年MM月dd日 HH:mm ");
                EditText editText1 =(EditText) findViewById (R.id.contact_name);
                String a=editText1.getText().toString();
                EditText editText2 =(EditText) findViewById (R.id.contact_number);
                String b=editText2.getText().toString();
                AVObject Contact = new AVObject("Public_Contact");
                Contact.put("P_name",a);
                Contact.put("P_number",b);
                Contact.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            finish();
                        }
                    }
                });
            }
        });
    }

    //最后一行失去焦点时，自动隐藏键盘
    private void setTouchListener(final add_public_contact context) {
        context.getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (context.getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
                } else {
                    imm.hideSoftInputFromWindow((context.findViewById(android.R.id.content)).getWindowToken(), 0);
                }
                return false;
            }
        });
    }

    //返回的点击事件
    public void ImageButtonClick(View v) {
        switch (v.getId()) {
            case R.id.return5:
                finish();
                break;
        }
    }


}
