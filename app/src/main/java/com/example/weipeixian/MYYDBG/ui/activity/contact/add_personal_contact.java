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
import com.example.weipeixian.MYYDBG.R;
import static com.example.weipeixian.MYYDBG.util.contactUtil.addContact;

public class add_personal_contact extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setContentView(R.layout.change_phone);
        setTouchListener(this);//只需调用这个方法即可
        Button btn2 = (Button) findViewById(R.id.submit);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText1 = (EditText) findViewById(R.id.contact_name);
                String name = editText1.getText().toString();
                EditText editText2 = (EditText) findViewById(R.id.contact_number);
                String number = editText2.getText().toString();
                addContact(name, number);
                finish();
            }
        });
    }
    //最后一行失去焦点时，自动隐藏键盘
    private void setTouchListener(final add_personal_contact context) {
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