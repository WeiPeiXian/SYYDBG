package com.example.weipeixian.MYYDBG.ui.activity.document;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.folderAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class change_folder extends BaseActivity {
    protected RecyclerView recyclerView;
    private folderAdapter itemAdapter;
    private AVObject mavObject;
    private List<AVObject> mlist = new ArrayList<>();
    String objid;
    @BindView(R.id.back)
    ImageButton back;
    @BindView(R.id.finish)
    Button finish;
    @BindView(R.id.title)
    TextView title;
    String backFolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_folder);
        ButterKnife.bind(this);

        recyclerView = (RecyclerView)findViewById(R.id.doc_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        objid = this.getIntent().getStringExtra("id");
        AVQuery<AVObject> query =new AVQuery<>("Document");
        query.getInBackground(objid, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                mavObject = avObject;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = getFolder();
                if(name.equals("个人文档"))
                    finish();
                else{
                    setFolder(backFolder);
                    update();
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int s = itemAdapter.getSelectedPos();
                if (s!=-1){
                    mavObject.put("Folder",mlist.get(s).getString("Name"));
                    mavObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if (e ==null){
                                finish();
                                Toast.makeText(change_folder.this,"转移成功",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        update();
    }
    public void update(){
        mlist.clear();
        AVQuery<AVObject> query = new AVQuery<>("Document");
        query.whereEqualTo("Folder",getFolder());
        query.whereEqualTo("CreatedBy", AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {

                if(e!=null){
                    Toast.makeText(change_folder.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else{
                    mlist.clear();
                    for (AVObject a:list){
                        if (a.getString("Type").equals("folder")){
                            mlist.add(a);
                        }
                    }
                    itemAdapter = new folderAdapter(change_folder.this,mlist);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            backFolder = mlist.get(position).getString("Folder");
                            setFolder(mlist.get(position).getString("Name"));
                            update();
                        }
                    });

                }
            }
        });
    }
    public String getFolder(){
        return title.getText().toString();
    }
    public void setFolder(String s){
        title.setText(s);
    }
}
