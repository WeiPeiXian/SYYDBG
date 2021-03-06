package com.example.weipeixian.MYYDBG.ui.activity;
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
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.PermissionListener;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.util.XmTools;

import java.util.List;

import cn.leancloud.chatkit.LCChatKit;

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
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.login);
      mUsernameET = (EditText) findViewById(R.id.chat_login_username);
      mPasswordET = (EditText) findViewById(R.id.chat_login_password);
      mSigninBtn = (Button) findViewById(R.id.chat_login_signin_btn);
      mSignupTV = (TextView) findViewById(R.id.chat_login_signup);
      mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
      AVUser currentUser = AVUser.getCurrentUser();
      if (currentUser != null) {
          if (hasWriteExternalStoragePermission()) {
              login_in_kit();
              String name =  currentUser.getUsername();
              Intent intent = new Intent(LoginActivity.this, open_page.class);
              startActivity(intent);
          } else {
              requestPermission();
          }
      }
      mPasswordCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
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
            requestPermission();
        }
      }

    });

    mSignupTV.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
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
      boolean b4 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PermissionChecker.PERMISSION_GRANTED;
      boolean b2 =  ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PermissionChecker.PERMISSION_GRANTED;
      boolean b3 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PermissionChecker.PERMISSION_GRANTED;
      boolean b1 =  ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED;
    return b1&&b2&&b3&&b4;
  }
    private void requestPermission(){
        requestRunTimePermission(new String[]{Manifest.permission.READ_SMS, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS}
                , new PermissionListener() {
                    @Override
                    public void onGranted() {  //所有权限授权成功
                        final String userName = mUsernameET.getText().toString().trim();
                        final String password = mPasswordET.getText().toString().trim();
                        login(userName,password);
                    }
                    @Override
                    public void onGranted(List<String> grantedPermission) { //授权失败权限集合
                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) { //授权成功权限集合
                    }
                });
    }

  public void login() {
    final String userName = mUsernameET.getText().toString().trim();
    final String password = mPasswordET.getText().toString().trim();
    if (TextUtils.isEmpty(userName)) {
      Toast.makeText(getApplicationContext(), "请输入用户名",
              Toast.LENGTH_SHORT).show();
    } else if (TextUtils.isEmpty(password)) {
      Toast.makeText(getApplicationContext(), "请输入密码",
              Toast.LENGTH_SHORT).show();
    } else {
        requestPermission();
    }
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
                            Intent intent = new Intent(LoginActivity.this, open_page.class);
                            login_in_kit();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    public void login_in_kit(){
        LCChatKit.getInstance().open(AVUser.getCurrentUser().getUsername(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (null == e) {
                    Toast.makeText(LoginActivity.this,"已获取内部短信", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this,"获取内部短信失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}