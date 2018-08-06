package com.example.weipeixian.MYYDBG.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.PermissionListener;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.model.PhoneInfo;
import com.example.weipeixian.MYYDBG.util.PhoneUtil;

import java.util.List;

public class MainActivity extends BaseActivity {





    private List<PhoneInfo> phoneInfos;

    private ListView lv_main_list;



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestPermission();
    }
    private void requestPermission(){
        requestRunTimePermission(new String[]{Manifest.permission.READ_CONTACTS}
                , new PermissionListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onGranted() {  //所有权限授权成功
                        check();;
                    }
                    @Override
                    public void onGranted(List<String> grantedPermission) { //授权失败权限集合
                    }
                    @Override
                    public void onDenied(List<String> deniedPermission) { //授权成功权限集合
                    }
                });
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void get(){
        Cursor mCursor = null;
        try{
            mCursor = getContentResolver().query(Uri.parse("content://sms/"), new String[] { "_id", "address", "read", "body", "thread_id" }, "read=?", new String[] { "0" }, "date desc");
            int nameColumn = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);//姓名
            Log.d("null", String.valueOf(mCursor));
            Log.d("SSSSSSSSSSSSSSSSSSSSSS",String.valueOf(mCursor));
            Log.d("SSSSSSSSSSSSSSSSSSSSSS",String.valueOf(nameColumn));

            if (mCursor.moveToFirst()) {
                Log.d("null", String.valueOf(mCursor));
                Log.d("null", String.valueOf(nameColumn));
                do {
                    int _inIndex = mCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    if (_inIndex != -1) {
                        String m = mCursor.getString(_inIndex);
                        Log.d("null", m);
                    }
                } while (mCursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (mCursor !=null){
                mCursor.close();
                Log.d("SSSSSSSSSSSSSSSSSSSSSS","SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
            }
        }
    }


    /**

     * 检查权限

     */

    private void check() {

        //判断是否有权限

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)

                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},201);

        }else{

            initViews();

        }

    }



    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==201){

            initViews();

        }else{

            return;

        }

    }



    private void initViews() {

        PhoneUtil phoneUtil = new PhoneUtil(this);

        phoneInfos = phoneUtil.getPhone();

        lv_main_list = (ListView) findViewById(R.id.lv_main_list);

        MyAdapter myAdapter = new MyAdapter();

        lv_main_list.setAdapter(myAdapter);

        //给listview增加点击事件

        /*lv_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //拨打电话

                Intent intent = new Intent();

                intent.setAction("android.intent.action.CALL");

                intent.addCategory(Intent.CATEGORY_DEFAULT);

                intent.setData(Uri.parse("tel:"+phoneInfos.get(position).getTelPhone()));

                startActivity(intent);

            }

        });*/

    }
    //自定义适配器

    private class MyAdapter extends BaseAdapter {



        @Override

        public int getCount() {

            return phoneInfos.size();

        }



        @Override

        public Object getItem(int position) {

            return phoneInfos.get(position);

        }



        @Override

        public long getItemId(int position) {

            return position;

        }



        @SuppressLint("NewApi")

        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            PhoneInfo phoneInfo = phoneInfos.get(position);

            LinearLayout linearLayout = new LinearLayout(MainActivity.this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.weight = 1;

            TextView tv_name = new TextView(MainActivity.this);

            tv_name.setId(View.generateViewId());

            tv_name.setLayoutParams(layoutParams);

            tv_name.setText(phoneInfo.getName());

            TextView tv_num = new TextView(MainActivity.this);

            tv_num.setId(View.generateViewId());

            tv_num.setLayoutParams(layoutParams);

            tv_num.setText(phoneInfo.getTelPhone());

            linearLayout.addView(tv_name);

            linearLayout.addView(tv_num);

            return linearLayout;

        }

    }

}
