/**
 * 
 */
package com.example.weipeixian.syydbg.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.weipeixian.syydbg.BaseActivity;
import com.example.weipeixian.syydbg.R;
import com.example.weipeixian.syydbg.util.XmTools;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * @author diyangxia
 * 
 */
public class LoginActivity extends BaseActivity {

	private EditText mUsernameET;
	private EditText mPasswordET;
	private Button mSigninBtn;
	private TextView mSignupTV;
	private CheckBox mPasswordCB;
	private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;
//	boolean islogin = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_login);

		mUsernameET = (EditText) findViewById(R.id.chat_login_username);
		mPasswordET = (EditText) findViewById(R.id.chat_login_password);
		mSigninBtn = (Button) findViewById(R.id.chat_login_signin_btn);
		mSignupTV = (TextView) findViewById(R.id.chat_login_signup);
		mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
		AVUser currentUser = AVUser.getCurrentUser();
//		if (currentUser != null) {
//			// 跳转到首页
//		} else {
//			//缓存用户对象为空时，可打开用户注册界面…
//		}
		if (EMClient.getInstance().isLoggedInBefore()) {
			Log.d("TAG", "已经登陆过");
			EMClient.getInstance().chatManager().loadAllConversations();//

			if (hasWriteExternalStoragePermission()) {
				startActivity(new Intent(LoginActivity.this, SMSHostActivity.class));
			}
			else{
				applyPermission();
			}

		}

		mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1) {
					mPasswordCB.setChecked(true);
					//动态设置密码是否可见
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

		mSigninBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (hasWriteExternalStoragePermission()) {
					login();
				}
				else{
					applyPermission();
				}
			}

		});

		mSignupTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(LoginActivity.this,
						RegisterActivity.class));
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			XmTools.showExitDialog(LoginActivity.this);
		}
		return super.onKeyDown(keyCode, event);
	}
	private boolean hasWriteExternalStoragePermission() {
		return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
	}

	private void applyPermission() {
		ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case REQUEST_WRITE_EXTERNAL_STORAGE:
				if (grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
					login();
				} else {
					Log.d("main", "没有权限！");
				}
				break;
		}
	}
	public void login() {
		// TODO Auto-generated method stub
		final String userName = mUsernameET.getText().toString().trim();
		final String password = mPasswordET.getText().toString().trim();

		if (TextUtils.isEmpty(userName)) {
			Toast.makeText(getApplicationContext(), "请输入用户名",
					Toast.LENGTH_SHORT).show();
		} else if (TextUtils.isEmpty(password)) {
			Toast.makeText(getApplicationContext(), "请输入密码",
					Toast.LENGTH_SHORT).show();
		} else {
			/**************************************************************/
			//leancloud登录

			/**************************************************************/

			/*****************************************************************/
			//环换登录
			EMClient.getInstance().login(userName, password,
					new EMCallBack() {// 回调
						@Override
						public void onSuccess() {
							runOnUiThread(new Runnable() {
								public void run() {
									//将所有会话导入本地
									EMClient.getInstance().chatManager().loadAllConversations();//
									Log.d("main", "登陆聊天服务器成功！");
									Toast.makeText(
											getApplicationContext(),
											"登陆成功", Toast.LENGTH_SHORT)
											.show();
									startActivity(new Intent(
											LoginActivity.this,
											SMSHostActivity.class));
								}
							});
						}

						@Override
						public void onProgress(int progress,
											   String status) {

						}

						@Override
						public void onError(int code, String message) {
							if (code == -1005) {
								message = "用户名或密码错误";
							}
							final String msg = message;
							runOnUiThread(new Runnable() {
								public void run() {
									Log.d("main", "登陆聊天服务器失败！");
									Toast.makeText(
											getApplicationContext(),
											msg, Toast.LENGTH_SHORT)
											.show();
								}
							});
						}
					});
			/**************************************************************/

		}
	}
	public void sendMessageToJerryFromSender(String sender){
		//登录这个啥鸡儿
		// Tom 用自己的名字作为clientId，获取AVIMClient对象实例
		AVIMClient tom = AVIMClient.getInstance(sender);
		// 与服务器连接
		tom.open(new AVIMClientCallback() {
			@Override
			public void done(AVIMClient client, AVIMException e) {
				if (e == null) {

				}
			}
		});
	}
	public void LoginByUser(){
		AVUser.logInInBackground("username", "password", new LogInCallback<AVUser>() {
			@Override
			public void done(AVUser user, AVException e) {
				if (null != e) {
					return;
				}
				// 与服务器连接
				AVIMClient client = AVIMClient.getInstance(user);
				client.open(new AVIMClientCallback() {
					@Override
					public void done(final AVIMClient avimClient, AVIMException e) {
						// do something as you need.
					}
				});
			}
		});
	}

}
