package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.util.CacheActivity;
import com.example.weipeixian.MYYDBG.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class new_flow extends AppCompatActivity{
    private LayoutInflater myinflater;
    private AVObject flow= new AVObject("Flow");
    Map<String,String> map =new LinkedHashMap<>();
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.next)
    TextView done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_set);
        ButterKnife.bind(this);
        if (!CacheActivity.activityList.contains(new_flow.this)) {
            CacheActivity.addActivity(new_flow.this);
        }
        flow.put("user", AVUser.getCurrentUser().getUsername());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        myinflater =new LayoutInflater(this) {
            @Override
            public LayoutInflater cloneInContext(Context context) {
                return null;
            }
        };
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flow.put("biaodan",map);
                flow.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null){
                            CacheActivity.finishActivity();
                        }
                    }
                });
            }
        });

        String objid = this.getIntent().getStringExtra("id");
        final LinearLayout linearLayout = findViewById(R.id.line);
        final AVObject[] avObject = new AVObject[1];
        AVQuery<AVObject> query = new AVQuery<>("Process");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e==null){
                    String mtitle =  avObject.getString("name");
                    title.setText(mtitle);
                    flow.put("name",mtitle);
                    String type =  avObject.getString("type");
                    flow.put("type",type);
                    System.out.print(avObject.getList("biaodan").toString());;
                    List<List<String>> list1 = avObject.getList("biaodan");
                    for (List<String> m:list1){
                        String[] strings = new String[]{
                                m.get(0),m.get(1)
                        };
                        addview(linearLayout,strings);
                    }
                    List<List<String>> lists =avObject.getList("buzhou");
                    Map<String,Boolean> map = new HashMap<>();
                    Map<String,Boolean> map1 =new HashMap<>();
                    for(List<String> mlist:lists){
                        map.put(mlist.get(1),false);
                        map1.put(mlist.get(1),false);
                    }
                    List<String> strings = new ArrayList<>(map.keySet());
                    flow.put("step",lists);
                    flow.put("state",map);
                    flow.put("judge",strings);
                    flow.put("result",map1);

                }
            }
        });


    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public void addview(LinearLayout linearLayout, String[] m){
        final String n = m[0];
        View mview = null;
        switch (m[1]){
            case "单行文本":
                mview = myinflater.inflate(R.layout.single_line,null);
                TextView name = mview.findViewById(R.id.title);
                name.setText(m[0]);
                EditText mname = mview.findViewById(R.id.hint);
                mname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        map.put(n,s.toString());
                        Log.d("sa",map.toString());
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        Log.e("输入前确认执行该方法", "开始输入");
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                        // 输入后的监听
                        Log.e("输入结束执行该方法", "输入结束");
                    }
                });
                linearLayout.addView(mview);
                break;
            case "多行文本":
                mview = myinflater.inflate(R.layout.multi_line,null);
                EditText medit = mview.findViewById(R.id.edit);
                linearLayout.addView(mview);
                medit.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        map.put(n,s.toString());
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    // 输入后的监听
                    }
                });
                medit.setHint(n);
                break;
            case "日期(年-月-日)":
                mview = myinflater.inflate(R.layout.date,null);
                TextView date_name = mview.findViewById(R.id.title);
                date_name.setText(n);
                final TextView date = mview.findViewById(R.id.date);
                linearLayout.addView(mview);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar c = Calendar.getInstance();
                        new DatePickerDialog(new_flow.this, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dat=year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
                                Log.d("sa",map.toString());
                                date.setText(dat);
                                map.put(n,dat);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });
                break;
        }

    }


}
