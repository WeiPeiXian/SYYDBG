package com.example.weipeixian.MYYDBG.ui.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;

public class FoundPassword extends BaseActivity implements View.OnClickListener{

    View send, verify;
    EditText phoneEdit, codeEdit;
    private Activity cxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_password);
        cxt = this;
        findView();
    }

    private void findView() {
        send = findViewById(R.id.proving);
        verify = findViewById(R.id.verify);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        codeEdit = (EditText) findViewById(R.id.codeEdit);
        send.setOnClickListener(this);
        verify.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.proving) {
            final String phone = phoneEdit.getText().toString();
            sendCode(phone);
        } else {
            final String code = codeEdit.getText().toString();
            verifyCode(code);
        }
    }

    private void verifyCode(String code) {
        AVOSCloud.verifySMSCodeInBackground(code, phoneEdit.getText().toString(),
                new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Toast.makeText(FoundPassword.this,"验证成功",Toast.LENGTH_SHORT).show();
                        } else {
                            e.printStackTrace();
                            Toast.makeText(FoundPassword.this,"验证失败",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void sendCode(final String phone) {
        new AsyncTask<Void, Void, Void>() {
            boolean res;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    AVOSCloud.requestSMSCode(phone, "SYYDBG", "绵阳移动办公系统", 10);
                    res = true;
                } catch (AVException e) {
                    e.printStackTrace();
                    res = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (res) {
                    Toast.makeText(FoundPassword.this,"发送成功",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(FoundPassword.this,"发送失败",Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
