package com.example.weipeixian.MYYDBG.ui.activity.process;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.Fragment.process.Step3Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class process_detail extends BaseActivity {
    String id;
    String type1;
    private Handler handler;
    private Map<Integer,String[]> map = new LinkedHashMap<>();
    private Map<Integer,String[]> map2 = new LinkedHashMap<>();

    final int[] i = new int[1];
    final int[] j = new int[1];

    AVObject mavObject;
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.add1)
    Button add_biaodan;
    @BindView(R.id.add2)
    Button add_buzhou;
    @BindView(R.id.line)
    LinearLayout biaodan;
    @BindView(R.id.line2)
    LinearLayout buzhou;
    @BindView(R.id.spinner1)
    Spinner spinner;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.next)
    TextView finish;
    private String phone;
    private String contact_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.process_manage);
        ButterKnife.bind(this);
        id=getIntent().getStringExtra("id");
        i[0]=0;
        j[0]=0;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }
    public void init(){
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mavObject.put("type",type1);
                List<String[]> list = new ArrayList<>(map2.values());
                mavObject.put("buzhou",list);
                List<String[]> list1 = new ArrayList<>(map.values());
                mavObject.put("biaodan",list1);
                mavObject.put("name",name.getText().toString());
                mavObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null){
                            finish();
                        }
                    }
                });
            }
        });
        add_biaodan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(process_detail.this);
                builder.setTitle("添加字段");
                View myview = getLayoutInflater().inflate(R.layout.dialog_view, null);
                builder.setView(myview);
                final EditText name = myview.findViewById(R.id.edit);
                Spinner type = myview.findViewById(R.id.spinner);
                String[] mItems =getResources().getStringArray( R.array.process_data);
                final String[] type1 = new String[1];
                final String[] finalMItems = mItems;
                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        type1[0] = finalMItems[pos];
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which)
                    {
                        addview_biaodan(name.getText().toString(),type1[0]);
                    }

                });
                builder.show();
            }
        });
        add_buzhou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_name = null;
                phone = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(process_detail.this);
                builder.setTitle("添加审批步骤");
                View myview = getLayoutInflater().inflate(R.layout.dialog_view2, null);
                builder.setView(myview);
                final EditText name = myview.findViewById(R.id.edit);
                final TextView textView2 = myview.findViewById(R.id.textView);
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setData(ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent,0);
                        handler =new Handler(){
                            public void handleMessage(Message msg) {
                                textView2.setText(contact_name);
                            };
                        };

                    }
                });
                final String[] type1 = new String[1];
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which)
                    {
                        addview(name.getText().toString(),contact_name);
                    }

                });
                builder.show();
            }
        });



        AVQuery query =new AVQuery("Process");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    mavObject = avObject;
                    name.setText(avObject.getString("name"));
                    Log.d("s",name.getText().toString());
                    String objid = "5b6d21179f54540035076027";
                    AVQuery<AVObject> query = new AVQuery<>("processType");
                    query.getInBackground(objid, new GetCallback<AVObject>() {
                        @Override
                        public void done(final AVObject avObject, AVException e) {
                            List<String> list = avObject.getList("type");
                            String[] m = new String[list.size()];
                            int j =0;
                            for (String x:list){
                                m[j] = x;
                                j++;
                            }
                            final String[] mItems = m;

                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(process_detail.this,android.R.layout.simple_spinner_item, mItems);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner .setAdapter(adapter);
                            String ty = mavObject.getString("type");
                            if (ty!=null){
                                SpinnerAdapter apsAdapter= spinner.getAdapter();
                                int k= apsAdapter.getCount();
                                for(int i=0;i<k;i++){
                                    if(ty.equals(apsAdapter.getItem(i).toString())){
                                        spinner.setSelection(i);
                                        break;
                                    }
                                }
                            }
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                                    type1 = mItems[pos];
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    });
                    List<List<String>> list = avObject.getList("biaodan");
                    if (list!=null){
                        for (List<String> m:list){
                            addview_biaodan(m.get(0),m.get(1));
                        }
                    }

                    List<List<String>>  list2 = avObject.getList("buzhou");
                    if (list2!=null){
                        for (List<String> m:list2){
                            contact_name = m.get(2);
                            phone =m.get(1);
                            addview(m.get(0),m.get(2));
                        }
                    }

                }
                else {
                    Log.d("Sssssssssssssss",e.getMessage());
                }
            }
        });

    }
    public void addview_biaodan(String name,String type) {
        final View mview = getLayoutInflater().inflate(R.layout.add_view, null);
        final TextView mname = mview.findViewById(R.id.name);
        final TextView mtype = mview.findViewById(R.id.type);
        mname.setText(name);
        mtype.setText(type);
        final String[] m = new String[]{name, type};
        map.put(i[0], m);
        biaodan.addView(mview);
        mview.setId(i[0]);
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(process_detail.this);
                builder.setTitle("修改字段");
                final View myview = getLayoutInflater().inflate(R.layout.dialog_view, null);
                builder.setView(myview);
                final EditText name = myview.findViewById(R.id.edit);
                final Spinner type = myview.findViewById(R.id.spinner);
                String[] mItems = getResources().getStringArray(R.array.process_data);
                final String[] type1 = new String[1];
                final String[] finalMItems = mItems;
                name.setText(mname.getText().toString());
                type1[0] = m[1];
                SpinnerAdapter apsAdapter = type.getAdapter(); //得到SpinnerAdapter对象
                int k = apsAdapter.getCount();
                for (int i = 0; i < k; i++) {
                    if (type1[0].equals(apsAdapter.getItem(i).toString())) {
                        type.setSelection(i);
                        break;
                    }
                }
                type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int pos, long id) {
                        type1[0] = finalMItems[pos];
                        m[1] = finalMItems[pos];
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        String[] string = new String[]{name.getText().toString(), type1[0]};
                        map.put(mview.getId(), string);
                        mname.setText(name.getText().toString());
                        mtype.setText(type1[0]);

                    }

                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(process_detail.this, "删除", Toast.LENGTH_LONG).show();
                        map.remove(mview.getId());
                        biaodan.removeView(mview);
                    }

                });
                builder.show();
            }

            ;
        });
        i[0]++;
    }



    public void addview(String name,String type) {
        final View mview = getLayoutInflater().inflate(R.layout.add_view2, null);
        final TextView mname = mview.findViewById(R.id.name);
        final TextView mtype = mview.findViewById(R.id.type);
        mname.setText(name);
        mtype.setText(type);
        final String[] m = new String[]{name, phone,contact_name};
        map2.put(j[0], m);
        buzhou.addView(mview);
        mview.setId(j[0]);
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(process_detail.this);
                builder.setTitle("修改审批步骤");
                View myview = getLayoutInflater().inflate(R.layout.dialog_view2, null);
                builder.setView(myview);
                contact_name = map2.get(mview.getId())[2];
                phone =  map2.get(mview.getId())[1];
                final EditText name = myview.findViewById(R.id.edit);
                name.setText(mname.getText().toString());
                final TextView textView2 = myview.findViewById(R.id.textView);
                textView2.setText(contact_name);
                textView2.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_PICK);
                        intent.setData(ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent,0);
                        handler =new Handler(){
                            public void handleMessage(Message msg) {
                                textView2.setText(contact_name);
                            };
                        };

                    }
                });
                final String[] type1 = new String[1];
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which)
                    {
                        String[] string = new String[]{name.getText().toString(), phone,contact_name};
                        map2.put(mview.getId(), string);
                        mname.setText(name.getText().toString());
                        mtype.setText(contact_name);

                    }

                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(process_detail.this, "删除", Toast.LENGTH_LONG).show();
                        map2.remove(mview.getId());
                        buzhou.removeView(mview);
                    }

                });
                builder.show();
            }

            ;
        });
        j[0]++;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode== AppCompatActivity.RESULT_OK){
                    ReadContactMsg readContactMsg = new ReadContactMsg(this,data);
                    contact_name = readContactMsg.getName();
                    phone = readContactMsg.getPhone();
                    handler.sendEmptyMessageAtTime(0,0);
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
