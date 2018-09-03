package com.example.weipeixian.MYYDBG.ui.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Change_public_Contact extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    AVObject Contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.change_phone);
        ButterKnife.bind(this);
        title.setText("修改联系人");
        setTouchListener(this);//只需调用这个方法即可
        Intent intent = getIntent();
        String phone_name1 = intent.getStringExtra("extra_name");
        String phone_number1 = intent.getStringExtra("extra_number");
        final String id = intent.getStringExtra("extra_objectId");
        EditText changePhone_name = (EditText) findViewById(R.id.contact_name);
        EditText changePhone_number = (EditText) findViewById(R.id.contact_number);
        changePhone_name.setText(phone_name1);
        changePhone_number.setText(phone_number1);
        Button btn = (Button) findViewById(R.id.submit);
        AVQuery<AVObject> query =new AVQuery<>("Public_Contact");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    Contact = avObject;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 =(EditText) findViewById (R.id.contact_name);
                String a=editText1.getText().toString();
                EditText editText2 =(EditText) findViewById (R.id.contact_number);
                String b=editText2.getText().toString();
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
    //返回的点击事件
    public void ImageButtonClick(View v) {
        switch (v.getId()) {
            case R.id.return5:
                finish();
                break;
        }
    }
    private void setTouchListener(final Change_public_Contact context) {
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
}

