package com.example.weipeixian.MYYDBG.ui.activity.notice;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChangeNotice extends AppCompatActivity {
    String id;
    AVObject mavobject;
    private EditText dateEdit;
    // 定义显示时间控件
    private Calendar calendar; // 通过Calendar获取系统时间
    private int mYear;
    private int mMonth;
    private int mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 锁定屏幕
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.change_list);
        setTouchListener(this);//只需调用这个方法即可
        //从上个页面中获取数据
        Intent intent = getIntent();
        id = intent.getStringExtra("id");



        final EditText changeNotice_title = (EditText) findViewById(R.id.annoucement_title);
        final EditText changeNotice_time = (EditText) findViewById(R.id.end_date);
        final EditText changeNotice_matter = (EditText) findViewById(R.id.annoucement);
        AVQuery<AVObject> query =new AVQuery<>("Notice");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    mavobject = avObject;
                    changeNotice_title.setText(avObject.getString("N_title"));
                    Date date = avObject.getCreatedAt();
                    changeNotice_time.setText(avObject.getString("N_time"));
                    changeNotice_matter.setText(avObject.getString("N_matter"));


                }
            }
        });
        dateEdit = (EditText) findViewById(R.id.end_date);
        calendar = Calendar.getInstance();
        // 点击"日期"按钮布局 设置日期
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ChangeNotice.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                mYear = year;
                                mMonth = month;
                                mDay = day;
                                // 更新EditText控件日期 小于10加0
                                dateEdit.setText(new StringBuilder()
                                        .append(mYear)
                                        .append("-")
                                        .append((mMonth + 1) < 10 ? "0"
                                                + (mMonth + 1) : (mMonth + 1))
                                        .append("-")
                                        .append((mDay < 10) ? "0" + mDay : mDay));
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //点击提交的响应事件
        Button btn = (Button) findViewById(R.id.submit_Notice);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年MM月dd日 HH:mm:ss ");
                //获取当前时间
                Date curDate = new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                Log.d("test","click"+str);
                EditText editText1 =(EditText) findViewById (R.id.annoucement_title);
                String a=editText1.getText().toString();
                EditText editText2 =(EditText) findViewById (R.id.annoucement);
                String b=editText2.getText().toString();
                EditText editText3 =(EditText) findViewById (R.id.end_date);
                String c=editText3.getText().toString();
                mavobject.put("N_title",a);
                mavobject.put("N_matter",b);
                mavobject.put("N_time",c);
                mavobject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e == null){
                            Log.d("saved","success!");
                            finish();
                        }
                    }
                });
            }
        });
    }
    //返回的点击事件
    public void ImageButtonClick(View v) {
        switch (v.getId()) {
            case R.id.return5:
                finish();
                break;
        }
    }
    //最后一行失去焦点时，自动隐藏键盘
    private void setTouchListener(final ChangeNotice context) {
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
}
