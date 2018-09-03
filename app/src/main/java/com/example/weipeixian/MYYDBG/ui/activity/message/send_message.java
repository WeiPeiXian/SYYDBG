package com.example.weipeixian.MYYDBG.ui.activity.message;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.leancloud.chatkit.utils.LCIMConstants;
import cn.leancloud.chatkit.view.LCIMInputBottomBar;

public class send_message extends BaseActivity{
    private EditText sendperson;
    protected LCIMInputBottomBar inputBottomBar;
    private EditText content;
    private ImageButton send;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.chat_person_btn)
    ImageButton add;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_inner_message);
        ButterKnife.bind(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendperson = (EditText)findViewById(R.id.sendperson);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,0);
            }
        });
        sendperson.addTextChangedListener(new TextWatcher() {
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

        send  =(ImageButton)findViewById(R.id.send);
        content =(EditText)findViewById(R.id.chat_content);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = content.getText().toString();

                if (phone==null){
                    Toast.makeText(send_message.this,"接收人不能为空",Toast.LENGTH_SHORT).show();
                }
                else if (value==null){
                    Toast.makeText(send_message.this,"内容",Toast.LENGTH_SHORT).show();
                }
                else{
                    send_message(phone,value);
                }

            }
        });
    }
    public void send_message(String name, String content){
        Intent intent = new Intent(this, conversation.class);
        List<String> list = new ArrayList<String>();
        intent.putExtra(LCIMConstants.PEER_ID, name);
        intent.putExtra("content",content);
        intent.putExtra("name",name);
        intent.putExtra("jieshou",sendperson.getText().toString());
        startActivity(intent);
        finish();
    }
    ReadContactMsg readContactMsg;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode== AppCompatActivity.RESULT_OK){
                    readContactMsg = new ReadContactMsg(this,data);
                    sendperson .setText(readContactMsg.getName());
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
