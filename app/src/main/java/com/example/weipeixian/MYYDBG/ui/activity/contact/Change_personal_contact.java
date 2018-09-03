package com.example.weipeixian.MYYDBG.ui.activity.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weipeixian.MYYDBG.util.AppManager;
import com.example.weipeixian.MYYDBG.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Change_personal_contact extends Activity {
    @BindView(R.id.title)
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_phone);
        AppManager.addActivity(this);
        ButterKnife.bind(this);
        title.setText("修改联系人");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTouchListener(this);//只需调用这个方法即可
        Intent intent = getIntent();
        String phone_name1 = intent.getStringExtra("extra_name");
        String phone_number1 = intent.getStringExtra("extra_number");
        EditText changePhone_name = (EditText) findViewById(R.id.contact_name);
        EditText changePhone_number = (EditText) findViewById(R.id.contact_number);
        changePhone_name.setText(phone_name1);
        changePhone_number.setText(phone_number1);
        Button btn = (Button) findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String phone_name1 = intent.getStringExtra("extra_name");
                EditText editText1 = (EditText) findViewById(R.id.contact_name);
                String name = editText1.getText().toString();
                EditText editText2 = (EditText) findViewById(R.id.contact_number);
                String number = editText2.getText().toString();
                deleteContactPhoneNumber(phone_name1);
                addContact(name, number);
                AppManager.killAllActivity();
                startActivity(new Intent(Change_personal_contact.this,person_contact_detail.class).putExtra("extra_name",name).putExtra("extra_number",number));
            }
        });
    }
    //返回的点击事件
    public void ImageButtonClick(View v) {
        switch (v.getId()) {
            case R.id.return5:
                AppManager.killActivity(this);
                break;
        }
    }
    private void setTouchListener(final Change_personal_contact context) {
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
    // 一个添加联系人信息的例子

    public void addContact(String name, String phoneNumber) {
        ContentValues values = new ContentValues();
        Uri rawContactUri = getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, values);
        long rawContactId = ContentUris.parseId(rawContactUri);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        // 内容类型
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, name);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber);
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        values.clear();
        values.put(ContactsContract.Data.RAW_CONTACT_ID, rawContactId);
        values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Email.DATA, "zhangphil@xxx.com");
        values.put(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK);
        getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
        Toast.makeText(this, "联系人数据添加成功", Toast.LENGTH_SHORT).show();
    }
    private void deleteContactPhoneNumber(String contactName) {
        Uri uri = ContactsContract.RawContacts.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        String where = ContactsContract.PhoneLookup.DISPLAY_NAME;
        Cursor cursor = resolver.query(uri, new String[]{ContactsContract.Data._ID}, where + "=?", new String[]{contactName}, null);
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            //根据id删除data中的相应数据
            resolver.delete(uri, where + "=?", new String[]{contactName});
            uri = ContactsContract.Data.CONTENT_URI;
            resolver.delete(uri, ContactsContract.Data.RAW_CONTACT_ID + "=?", new String[]{id + ""});
        }
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }


}