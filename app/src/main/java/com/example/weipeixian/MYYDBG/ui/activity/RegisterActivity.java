package com.example.weipeixian.MYYDBG.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

//import com.easemob.EMError;
//import com.easemob.chat.EMChatManager;
//import com.easemob.exceptions.EaseMobException;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.util.IdentifyCode;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.LCChatKit;

public class RegisterActivity extends BaseActivity {

    private EditText mUsernameET;
    private EditText mPasswordET;
    private EditText repeatPW;
    private Button mSignupBtn;
    private Handler mHandler;
    private ImageView mBackTV;
    @BindView(R.id.back)
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        ButterKnife.bind(this);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1000:
                        Toast.makeText(getApplicationContext(), "注册成功",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case 1001:
                        Toast.makeText(getApplicationContext(), "网络异常，请检查网络！",
                                Toast.LENGTH_SHORT).show();
                        break;


                    default:
                        break;
                }
            };
        };
        mUsernameET = (EditText) findViewById(R.id.user_register_username);
        mPasswordET = (EditText) findViewById(R.id.register_password);
        mSignupBtn = (Button) findViewById(R.id.user_register_password);
        repeatPW = (EditText) findViewById(R.id.register_password2);
        mBackTV = (ImageView) findViewById(R.id.back);
        mBackTV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        mSignupBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String userName = mUsernameET.getText().toString().trim();
                final String password = mPasswordET.getText().toString().trim();
                final String repeat = repeatPW.getText().toString().trim();
                if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(getApplicationContext(), "请输入用户名",
                            Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "请输入密码",
                            Toast.LENGTH_SHORT).show();
                }else if(!password.equals(repeat)){
                    Toast.makeText(getApplicationContext(), "两次密码不一致",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AVUser user = new AVUser();// 新建 AVUser 对象实例
                            user.setUsername(userName);// 设置用户名
                            user.setPassword(password);// 设置密码
                            user.put("role","一般用户");
                            user.signUpInBackground(new SignUpCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        mHandler.sendEmptyMessage(1000);
                                        login(userName,password);
                                    } else {
                                        Toast.makeText(RegisterActivity.this,"注册失败",Toast.LENGTH_SHORT);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
    public void login(String userName,String password ){
        AVUser.logInInBackground(userName, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser user, AVException e) {
                if (null != e) {

                    return;
                }
                LCChatKit.getInstance().open(AVUser.getCurrentUser().getUsername(), new AVIMClientCallback() {
                    @Override
                    public void done(AVIMClient avimClient, AVIMException e) {
                        if (null == e) {
                            Intent intent = new Intent(RegisterActivity.this, open_page.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}