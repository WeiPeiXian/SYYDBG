package com.example.weipeixian.syydbg.ui.activity;
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
    // TODO Auto-generated method stub
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_chat_login);
      mUsernameET = (EditText) findViewById(R.id.chat_login_username);
      mPasswordET = (EditText) findViewById(R.id.chat_login_password);
      mSigninBtn = (Button) findViewById(R.id.chat_login_signin_btn);
      mSignupTV = (TextView) findViewById(R.id.chat_login_signup);
      mPasswordCB = (CheckBox) findViewById(R.id.chat_login_password_checkbox);
      AVUser currentUser = AVUser.getCurrentUser();
      if (currentUser != null) {
          if (hasWriteExternalStoragePermission()) {
              login_in_kit();
              Intent intent = new Intent(LoginActivity.this, open_page.class);
              startActivity(intent);
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
        login(userName,password);
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
                    Toast.makeText(LoginActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}