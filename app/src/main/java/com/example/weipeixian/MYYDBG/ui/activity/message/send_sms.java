package com.example.weipeixian.MYYDBG.ui.activity.message;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.weipeixian.MYYDBG.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class send_sms extends Activity {
    ImageButton sendBtn;
    EditText txtphoneNo;
    EditText txtMessage;
    ReadContactMsg readContactMsg;
    String phone;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.add)
    ImageButton add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_phone_message);
        ButterKnife.bind(this);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendBtn = (ImageButton) findViewById(R.id.send);
        txtphoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        txtphoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phone = s.toString();
                Log.e("输入过程中执行该方法", "文字变化");
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                Log.e("输入前确认执行该方法", "开始输入");

            }
            @Override
            public void afterTextChanged(Editable s) {
                Log.e("输入结束执行该方法", "输入结束");
            }
        });
        txtMessage = (EditText) findViewById(R.id.chat_content);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sendSMSMessage();
            }
        });
    }
    protected void sendSMSMessage() {
        String phoneNo = phone;
        String message = txtMessage.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
            txtMessage.setText("");
            finish();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "发送失败",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode== AppCompatActivity.RESULT_OK){
                    readContactMsg = new ReadContactMsg(this,data);
                    txtphoneNo .setText(readContactMsg.getName());
                    phone = readContactMsg.getPhone();
                }
                break;
        }
    }

    class ReadContactMsg{
        private String name;
        private String phone;
        public ReadContactMsg(Context context, Intent data){
            super();
            Uri contactData = data.getData();
            CursorLoader cursorLoader = new CursorLoader(context,contactData,null,null,null,null);
            Cursor cursor = cursorLoader.loadInBackground();
            if(cursor.moveToFirst()){
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
                phone = "此联系人暂未存入号码";
                Cursor phones = getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                        null,
                        null);
                if (phones.moveToFirst()) {
                    phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
            }
            cursor.close();
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }
    }

}
