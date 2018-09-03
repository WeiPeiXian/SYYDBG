package com.example.weipeixian.MYYDBG.ui.Fragment.process;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVObject;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.activity.process.add_process;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.navigation.Navigation;

public class Step2Fragment extends BaseFragment {
    private Map<Integer,String[]> map = new LinkedHashMap<>();
    private Button add;
    private TextView textView;
    private LayoutInflater myinflater;
    private LinearLayout linear;
    private View view;
    final int[] i = new int[1];
    public Step2Fragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        ImageButton back = view.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Navigation.findNavController(v).navigate(R.id.back_to_one);
                List<String[]> list = new ArrayList<>(map.values());
                avObject.put("biaodan",list);
                ((add_process)getActivity()).setAVObject(avObject);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.to_three);
                List<String[]> list = new ArrayList<>(map.values());
                avObject.put("biaodan",list);
                ((add_process)getActivity()).setAVObject(avObject);
            }
        });
        List<String[]> list = avObject.getList("biaodan");
        if (list!=null){
            for (String[] m:list){
                addview(m[0],m[1]);
            }
        }
        TextView title =view.findViewById(R.id.title);
        title.setText("配置表单");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("添加字段");
                View myview = myinflater.inflate(R.layout.dialog_view, null);
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
                        addview(name.getText().toString(),type1[0]);
                    }

                });
                builder.show();
            }
        });
    }
    public void addview(String name,String type) {
        final View mview = myinflater.inflate(R.layout.add_view, null);
        final TextView mname = mview.findViewById(R.id.name);
        final TextView mtype = mview.findViewById(R.id.type);
        mname.setText(name);
        mtype.setText(type);
        final String[] m = new String[]{name, type};
        map.put(i[0], m);
        linear.addView(mview);
        mview.setId(i[0]);
        mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("修改字段");
                final View myview = myinflater.inflate(R.layout.dialog_view, null);
                builder.setView(myview);
                final EditText name = myview.findViewById(R.id.edit);
                final Spinner type = myview.findViewById(R.id.spinner);
                String[] mItems = getResources().getStringArray(R.array.process_data);
                final String[] type1 = new String[1];
                final String[] finalMItems = mItems;
                //spinner初始化//
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
                        Toast.makeText(getActivity(), "删除", Toast.LENGTH_LONG).show();
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
}
