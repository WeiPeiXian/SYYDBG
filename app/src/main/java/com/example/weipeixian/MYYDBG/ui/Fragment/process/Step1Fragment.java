package com.example.weipeixian.MYYDBG.ui.Fragment.process;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.activity.process.add_process;


import java.util.List;

import androidx.navigation.Navigation;

public class Step1Fragment extends BaseFragment {
    String type1;
    ImageButton back;
    public Step1Fragment() { }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_one, container, false);
    }
    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        back= view.findViewById(R.id.back);
        final AVObject avObject = ((add_process)getActivity()).getAVObject();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        TextView btnNext = view.findViewById(R.id.next);
        final EditText name = view.findViewById(R.id.editname);
        if (name!=null)
            name.setText(avObject.getString("name"));
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.next_action);
                AVObject avObject = ((add_process)getActivity()).getAVObject();
                String mname = name.getText().toString();
                avObject.put("name",mname);
                avObject.put("type",type1);
                ((add_process)getActivity()).setAVObject(avObject);
            }
        });
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
        String objid = "5b6d21179f54540035076027";
        AVQuery<AVObject> query = new AVQuery<>("processType");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject mavObject, AVException e) {
                List<String> list = mavObject.getList("type");
                String[] m = new String[list.size()];
                int j =0;
                for (String x:list){
                    m[j] = x;
                    j++;
                }
                final String[] mItems = m;
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner .setAdapter(adapter);
                TextView title =view.findViewById(R.id.title);
                title.setText("填写流程名称");
                String ty = avObject.getString("type");
                if (ty!=null){
                    SpinnerAdapter apsAdapter= spinner.getAdapter();
                    int k= apsAdapter.getCount();
                    for(int i=0;i<k;i++){
                        if(avObject.getString("type").equals(apsAdapter.getItem(i).toString())){
                            spinner.setSelection(i);
                            break;
                        }
                    }
                }
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        type1 = mItems[pos];
                        avObject.put("type",type1);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

    }



}
