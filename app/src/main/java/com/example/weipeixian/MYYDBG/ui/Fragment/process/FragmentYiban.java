package com.example.weipeixian.MYYDBG.ui.Fragment.process;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.flowAdapter;
import com.example.weipeixian.MYYDBG.ui.activity.process.flow_monitor;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FragmentYiban extends BaseFragment {
    RecyclerView recyclerView;
    flowAdapter itemAdapter;
    View view;
    public FragmentYiban() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flow, container, false);
        return inflater.inflate(R.layout.fragment_flow, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        update();
    }
    public void onResume() {
        super.onResume();
        update();
    }
    private void update(){
        AVQuery<AVObject> query = new AVQuery<>("Flow");
        query.whereEqualTo("judge", AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (e!=null){
                    System.out.print(e.getMessage());
                }
                else {
                    final List<AVObject> m = new ArrayList<>();
                    for (AVObject l:list){
                        boolean x = l.getBoolean("banjie");
                        if (!x){

                            Log.d("banjie","false");
                            Map<String,Boolean> map = l.getMap("state");
                            if (map.get(AVUser.getCurrentUser().getUsername())){
                                m.add(l);
                            }
                        }

                    }
                    itemAdapter = new flowAdapter(getActivity(),m);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            AVObject avObject = m.get(i);
                            startActivity(new Intent(getActivity(), flow_monitor.class).putExtra("from",2).putExtra("id",avObject.getObjectId()));
                        }
                    });
                }
            }
        });
        recyclerView.setHasFixedSize(true);
    }
}
