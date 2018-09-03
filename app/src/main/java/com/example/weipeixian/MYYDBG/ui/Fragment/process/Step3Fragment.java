package com.example.weipeixian.MYYDBG.ui.Fragment.process;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.activity.process.add_process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;

public class Step3Fragment extends BaseFragment {

    private Map<Integer,String[]> map = new LinkedHashMap<>();
    private Button add;
    private TextView textView;
    private LayoutInflater myinflater;
    private LinearLayout linear;
    private View view;
    private String contact_name;
    private String phone;
    private Handler handler;

    final int[] i = new int[1];
    private ReadContactMsg readContactMsg;
    public Step3Fragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        myinflater = inflater;
        view = inflater.inflate(R.layout.add_process_frament, container, false);
        i[0]=1;
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add = view.findViewById(R.id.add);
        linear=(LinearLayout) view.findViewById(R.id.line);
        textView = view.findViewById(R.id.next);
        final AVObject avObject = ((add_process)getActivity()).getAVObject();
        add.setText("添加审批人步骤+");
        TextView btnNext = view.findViewById(R.id.next);
        btnNext.setText("完成");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交
            }
        });
        TextView title =view.findViewById(R.id.title);
        title.setText("创建审批人步骤");
        ImageButton back = view.findViewById(R.id.back);
        List<String[]> list = avObject.getList("buzhou");
        if (list!=null){
            for (String[] m:list){
                contact_name = m[2];
                phone =m[1];
                addview(m[0],m[2]);
            }
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact_name = null;
                phone = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加审批步骤");
                View myview = myinflater.inflate(R.layout.dialog_view2, null);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.back_to_two);
                List<String[]> list = new ArrayList<>(map.values());
                avObject.put("buzhou",list);
                ((add_process)getActivity()).setAVObject(avObject);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String[]> list = new ArrayList<>(map.values());
                avObject.put("buzhou",list);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null){
                            getActivity().finish();
                        }
                    }
                });
            }
        });

    }
    public void addview(String name,String type) {
        final View mview = myinflater.inflate(R.layout.add_view2, null);
        final TextView mname = mview.findViewById(R.id.name);
        final TextView mtype = mview.findViewById(R.id.type);
        mname.setText(name);
        mtype.setText(type);
        final String[] m = new String[]{name, phone,contact_name};
        map.put(i[0], m);
        linear.addView(mview);
        mview.setId(i[0]);
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("修改审批步骤");
                View myview = myinflater.inflate(R.layout.dialog_view2, null);
                builder.setView(myview);
                contact_name = map.get(mview.getId())[2];
                phone =  map.get(mview.getId())[1];
                final EditText name = myview.findViewById(R.id.edit);
                name.setText(mname.getText().toString());
                final TextView textView2 = myview.findViewById(R.id.textView);
                textView2.setText(mtype.getText().toString());
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
                        map.put(mview.getId(), string);
                        mname.setText(name.getText().toString());
                        mtype.setText(contact_name);

                    }

                });
                builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        map.remove(mview.getId());
                        linear.removeView(mview);
                    }

                });
                builder.show();
            }

            ;
        });
        i[0]++;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if(resultCode== AppCompatActivity.RESULT_OK){
                    readContactMsg = new ReadContactMsg(getActivity(),data);
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
                Cursor phones = getActivity().getContentResolver().query(
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
