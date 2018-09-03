package com.example.weipeixian.MYYDBG.ui.activity.contact;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weipeixian.MYYDBG.util.AppManager;
import com.example.weipeixian.MYYDBG.R;


public class person_contact_detail extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);
        AppManager.addActivity(this);
        Intent intent = getIntent();
        final String data_name = intent.getStringExtra("extra_name");
        final String data_number = intent.getStringExtra("extra_number");
        TextView newPhone_name = (TextView) findViewById(R.id.phone_name);
        TextView newPhone_number = (TextView) findViewById(R.id.phone_number);
        newPhone_name.setText(data_name);
        newPhone_number.setText(data_number);
    }
    public void ImageButtonClick(View v) {
        switch (v.getId()) {
            case R.id.return3:
                AppManager.killActivity(this);
                break;
            case R.id.new_phone_message:
                final ImageButton button = (ImageButton) findViewById(R.id.new_phone_message);
                showPopupMenu(button);
                break;
        }
    }
    private void deleteContactPhoneNumber(String contactName) {
        //根据姓名求id
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
        AppManager.killAllActivity();
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.option_menu_contact, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_unKnow) {  //根据ItemId进行判断。
                    AlertDialog.Builder alterDialog = new AlertDialog.Builder(person_contact_detail.this);
                    alterDialog.setMessage("确认删除？");
                    alterDialog.setCancelable(false);
                    alterDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = getIntent();
                            final String data_name = intent.getStringExtra("extra_name");
                            deleteContactPhoneNumber(data_name);
                        }
                    });
                    alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alterDialog.show();
                    return true;
                } else if (item.getItemId() == R.id.action_startActivity) {
                    String data_name = getIntent().getStringExtra("extra_name");
                    String data_number = getIntent().getStringExtra("extra_number");
                    Intent intent = new Intent();
                    intent.putExtra("extra_name",data_name);
                    intent.putExtra("extra_number",data_number);
                    intent.setClass(person_contact_detail.this, Change_personal_contact.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}

