package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.util.CacheActivity;
import com.example.weipeixian.MYYDBG.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class flowDetail extends BaseActivity {
    private LayoutInflater myinflater;
    private AVObject flow= new AVObject("Flow");
    Map<String,String> map =new LinkedHashMap<>();
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.flowname)
    TextView title;
    @BindView(R.id.name)
    TextView user;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.notok)
    Button notok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_monitor);
        ButterKnife.bind(this);
        if (!CacheActivity.activityList.contains(flowDetail.this)) {
            CacheActivity.addActivity(flowDetail.this);
        }
        ok.setVisibility(View.GONE);
        notok.setVisibility(View.GONE);
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

        String objid = this.getIntent().getStringExtra("id");
        final LinearLayout linearLayout = findViewById(R.id.line);
        AVQuery<AVObject> query = new AVQuery<>("Flow");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e==null){
                    flow = avObject;
                    user.setText(avObject.getString("user"));
                    String mtitle =  avObject.getString("name");
                    title.setText(mtitle);
                    View mview1 = null;
                    mview1 = myinflater.inflate(R.layout.flow_view,null);
                    TextView mname1 = mview1.findViewById(R.id.title);
                    mname1.setText("表单:");
                    linearLayout.addView(mview1);

                    Map<String,String> map1 = avObject.getMap("biaodan");
                    ListIterator<Map.Entry<String,String>> i=new ArrayList<Map.Entry<String,String>>(map1.entrySet()).listIterator(map1.size());
                    while(i.hasPrevious()) {
                        Map.Entry<String, String> entry=i.previous();
                        String[] m = new String[]{entry.getKey(),entry.getValue()};
                        addview(linearLayout,m);
                        System.out.println(entry.getKey()+":"+entry.getValue());
                    }
                    View mview = null;
                    mview = myinflater.inflate(R.layout.flow_view,null);
                    TextView mname = mview.findViewById(R.id.title);
                    mname.setText("审核步骤:");
                    linearLayout.addView(mview);


                    List<List<String>> lists = avObject.getList("step");
                    Map<String,Boolean> map3 = avObject.getMap("state");
                    Map<String,Boolean> map2 = avObject.getMap("result");
                    ListIterator<Map.Entry<String,Boolean>> i1=new ArrayList<Map.Entry<String,Boolean>>(map2.entrySet()).listIterator(map2.size());
                    while(i.hasPrevious()) {
                        Map.Entry<String, Boolean> entry=i1.previous();
                        String name=null;
                        String tongyi = null;
                        for (List<String> list :lists){
                            if (entry.getKey().equals(list.get(1))){
                                name = list.get(0);
                                Log.d("s",map3.get(entry.getKey()).toString());
                                if (map3.get(entry.getKey())){
                                    if (entry.getValue()){
                                        tongyi="同意";
                                    }
                                    else {
                                        tongyi = "拒绝";
                                    }
                                }
                                else
                                    tongyi = "未审批";

                            }
                        }
                        String[] m = new String[]{name,tongyi};
                        addview(linearLayout,m);

                    }
                    for (Map.Entry<String, Boolean> entry : map2.entrySet()) {
                        String name=null;
                        String tongyi = null;
                        for (List<String> list :lists){
                            if (entry.getKey().equals(list.get(1))){
                                name = list.get(0);
                                Log.d("s",map3.get(entry.getKey()).toString());
                                if (map3.get(entry.getKey())){
                                    if (entry.getValue()){
                                        tongyi="同意";
                                    }
                                    else {
                                        tongyi = "拒绝";
                                    }
                                }
                                else
                                    tongyi = "未审批";

                            }
                        }
                        String[] m = new String[]{name,tongyi};
                        addview(linearLayout,m);
                    }
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
        mview = myinflater.inflate(R.layout.flow_view,null);
        TextView name = mview.findViewById(R.id.title);
        name.setText(m[0]);
        TextView mname = mview.findViewById(R.id.hint);
        mname.setText(m[1]);
        linearLayout.addView(mview);

    }
}
