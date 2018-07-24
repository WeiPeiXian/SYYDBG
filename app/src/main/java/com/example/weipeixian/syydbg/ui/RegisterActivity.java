package com.example.weipeixian.syydbg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

//import com.easemob.EMError;
//import com.easemob.chat.EMChatManager;
//import com.easemob.exceptions.EaseMobException;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.weipeixian.syydbg.BaseActivity;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.util.IdentifyCode;

public class RegisterActivity extends BaseActivity {

	private EditText mUsernameET;
	private EditText mPasswordET;
	private EditText mCodeET;
	private Button mSignupBtn;
	private Handler mHandler;
	private CheckBox mPasswordCB;
	private ImageView mBackTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_register);

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
				case 1002:
					Toast.makeText(getApplicationContext(), "用户已存在！",
							Toast.LENGTH_SHORT).show();
					break;
				case 1003:
					Toast.makeText(getApplicationContext(), "注册失败，无权限",
							Toast.LENGTH_SHORT).show();
					break;
				case 1004:
					Toast.makeText(getApplicationContext(),
							"注册失败: " + (String) msg.obj, Toast.LENGTH_SHORT)
							.show();
					break;

				default:
					break;
				}
			};
		};

		mUsernameET = (EditText) findViewById(R.id.chat_register_username);
		mPasswordET = (EditText) findViewById(R.id.chat_register_password);
		mSignupBtn = (Button) findViewById(R.id.chat_register_signup_btn);
		mPasswordCB = (CheckBox) findViewById(R.id.chat_register_password_checkbox);
		mBackTV = (ImageView) findViewById(R.id.chat_register_back);




		mBackTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					mPasswordCB.setChecked(true);
					mPasswordET
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					mPasswordCB.setChecked(false);
					mPasswordET
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}
			}
		});
		mSignupBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String userName = mUsernameET.getText().toString().trim();
				final String password = mPasswordET.getText().toString().trim();
				final String code = mCodeET.getText().toString().trim();

				if (TextUtils.isEmpty(userName)) {
					Toast.makeText(getApplicationContext(), "请输入用户名",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "请输入密码",
							Toast.LENGTH_SHORT).show();
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							AVUser user = new AVUser();// 新建 AVUser 对象实例
							user.setUsername("userName");// 设置用户名
							user.setPassword("password");// 设置密码
//							user.setEmail("tom@leancloud.cn");// 设置邮箱
							user.signUpInBackground(new SignUpCallback() {
								@Override
								public void done(AVException e) {
									if (e == null) {
//										Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT);
										mHandler.sendEmptyMessage(1000);
										startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
									} else {
//										mHandler.sendEmptyMessage(1000);
										// 失败的原因可能有多种，常见的是用户名已经存在。
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
}
