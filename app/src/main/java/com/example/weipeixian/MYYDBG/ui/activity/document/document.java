package com.example.weipeixian.MYYDBG.ui.activity.document;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.documentAdapter;
import com.example.weipeixian.MYYDBG.ui.activity.notice.NoticeDetail;
import com.example.weipeixian.MYYDBG.ui.activity.open_page;
import com.example.weipeixian.MYYDBG.util.DocumentUtil;
import com.example.weipeixian.MYYDBG.util.XmTools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class document extends AppCompatActivity {
    private TextView tx;
    protected RecyclerView recyclerView;
    private documentAdapter itemAdapter;
    private AVFile file;
    private ProgressDialog pd;
    private Toolbar toolbar;
    private CharSequence title;
    private List<AVObject> mlist = new ArrayList<>();
    RadioGroup radioGroup;
    private String FOLDER = "folder";
    private String FILE = "file";
    private String backFolder;
    private boolean isAdministrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String role =  AVUser.getCurrentUser().getString("role");
        String name =  AVUser.getCurrentUser().getString("name");
        if (role!=null){
            if (role.equals("一般用户")){
                isAdministrator=false;
            }else{
                isAdministrator = true;
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);
        RelativeLayout layout = findViewById(R.id.layout);
        radioGroup = (RadioGroup) findViewById(R.id.main_radio);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        title = "个人文档";
        setTitle (title);

        recyclerView = (RecyclerView)findViewById(R.id.doc_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter = new documentAdapter(document.this,mlist);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_me:
                        setTitle("个人文档");
                        invalidateOptionsMenu();
                        update();
                        break;
                    case R.id.radio_pub:
                        setTitle("公共文档");
                        invalidateOptionsMenu();
                        update_public(true);
                        break;
                    case R.id.radio_do:
                        setTitle("文档模板");
                        update_moban();
                        invalidateOptionsMenu();
                        break;

                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radio_me:
                update();
                break;
            case R.id.radio_pub:
                if(getTitle().toString().equals("公共文档")){
                    update_public(true);
                }
                else {
                    update_public(false);
                }
                break;
            case R.id.radio_do:
                update_moban();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.document, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radio_me:
                menu.findItem(R.id.upload).setVisible(true);
                menu.findItem(R.id.add).setVisible(true);
                break;

            case R.id.radio_pub:
                menu.findItem(R.id.upload).setVisible(false);
                menu.findItem(R.id.add).setVisible(false);
                break;
            case R.id.radio_do:
            if(isAdministrator){
                menu.findItem(R.id.upload).setVisible(true);
                menu.findItem(R.id.add).setVisible(false);
                }else{
                menu.findItem(R.id.upload).setVisible(false);
                menu.findItem(R.id.add).setVisible(false);
            }
            break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            String name = getTitle().toString();
            if(name.equals("个人文档")||name.equals("公共文档")||name.equals("文档模板")){
                finish();
            }
            else{
                setTitle(backFolder);
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_me:
                        update();
                        break;
                    case R.id.radio_pub:
                        update_public(true);
                        break;
                    case R.id.radio_do:
                        update_moban();
                        break;
                }
            }

        }
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if(menuId==android.R.id.home){
            String name = getTitle().toString();
            if(name.equals("个人文档")||name.equals("公共文档")||name.equals("文档模板"))
                finish();
            else{
                setTitle(backFolder);
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.radio_me:
                        update();
                        break;
                    case R.id.radio_pub:
                        update_public(true);
                        break;
                    case R.id.radio_do:
                        update_moban();
                        break;
                }
            }
            return true;
        }
        if (menuId == R.id.upload) {
            pd=new ProgressDialog(document.this);
            pd.setTitle("上传");
            pd.setMessage("正在上传中，请稍后");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(true);


            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        } else if (menuId == R.id.add) {
            createfolder();
        }
        return super.onOptionsItemSelected(item);
    }
    public void update_public(final Boolean t){
        AVQuery<AVObject> query = new AVQuery<>("Document");
        query.whereEqualTo("Folder", "公共文档");
        if (t){
            query.whereNotEqualTo("CreatedBy",AVUser.getCurrentUser().getUsername());
        }
        else
            query.whereEqualTo("CreatedBy",AVUser.getCurrentUser().getUsername());

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e!=null){
                    Toast.makeText(document.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else{
                    mlist.clear();
                    if (t){
                        AVObject avObject = new AVObject("Document");
                        avObject.put("Type",FOLDER);
                        avObject.put("Folder","公共文档");
                        avObject.put("CreatedBy",AVUser.getCurrentUser().getUsername());
                        avObject.put("Name","我的文档");
                        mlist.add(avObject);
                    }
                    for (AVObject a:list){
                        if (a.getString("Type").equals("file")){
                            mlist.add(a);
                        }
                    }
                    itemAdapter = new documentAdapter(document.this,mlist);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (t){
                                if (position == 0){
                                    update_public(false);
                                    backFolder = "公共文档";
                                    setTitle("我的分享");
                                }
                            }
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AVObject avObject =  mlist.get(position);
                            if (avObject.getString("Type").equals("file")){
                                if(!t) {
                                    edit_my_file(avObject, new String[]{"下载", "删除"});
                                }else{
                                    edit_my_file(avObject, new String[]{"下载"});
                                }
                            }
                            return false;
                        }
                    });
                }
            }
        });
    }

    public void update_moban(){
        mlist.clear();
        AVQuery<AVObject> query = new AVQuery<>("Document");
        query.whereEqualTo("Folder", "文档模板");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e!=null){
                    Toast.makeText(document.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else{
                    mlist.clear();
                    mlist = list;
                    itemAdapter = new documentAdapter(document.this,mlist);
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AVObject avObject =  mlist.get(position);
                            if(isAdministrator) {
                                edit_my_file(avObject, new String[]{"下载", "删除"});
                            }else{
                                edit_my_file(avObject, new String[]{"下载"});
                            }
                            return false;
                        }
                    });
                }
            }
        });
    }
    public void createfolder(){
        final EditText et=new EditText(document.this);
        et.setHint("文件夹名");
        AlertDialog dialog = new AlertDialog.Builder(document.this)
        .setTitle("新建文件夹")
        .setView(et)
        .setPositiveButton("确定", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String name = et.getText().toString().trim();
                AVObject document = new AVObject("Document");// 构建对象
                document.put("Type",FOLDER);
                document.put("Folder",getfolder());
                document.put("CreatedBy",AVUser.getCurrentUser().getUsername());
                document.put("Name",name);
                document.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e ==null){
                            update();
                        }
                    }
                });
            }
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //不做任何事
            }
        }).create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

    }

    public  String getfolder(){
        String folder = getTitle().toString();
        if (folder.equals("我的分享"))
            folder = "公共文档";
        return folder;
    }
    public void update(){
        mlist.clear();
        AVQuery<AVObject> query = new AVQuery<>("Document");
        query.whereEqualTo("Folder", getfolder());
        query.whereEqualTo("CreatedBy",AVUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if(e!=null){
                    Toast.makeText(document.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                else{
                    mlist.clear();
                    for (AVObject a:list){
                        if (a.getString("Type").equals("folder")){
                            mlist.add(a);
                        }
                    }
                    for (AVObject a:list){
                        if (a.getString("Type").equals("file")){
                            mlist.add(a);
                        }
                    }
                    itemAdapter = new documentAdapter(document.this,mlist);
                    Log.d("s",mlist.toString());
                    recyclerView.setAdapter(itemAdapter);
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            AVObject avObject =  mlist.get(position);
                            if (avObject.getString("Type").equals("folder")){
                                backFolder = avObject.getString("Folder");
                                setTitle(avObject.getString("Name"));
                                update();
                            }
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AVObject avObject =  mlist.get(position);
                            if (avObject.getString("Type").equals("folder")){
                                edit_my_file(avObject, new String[]{ "转移","删除"});
                            }
                            else
                                edit_my_file(avObject, new String[]{"下载", "转移","共享","删除"});

                            return false;
                        }
                    });
                }

            }
        });

    }
    public void setBackFolder(){
        backFolder = getTitle().toString();
    }
    public void edit_my_file(final AVObject avObject, final String[] s){
        AlertDialog dialog = new AlertDialog.Builder(document.this)
                .setTitle("操作").setItems(s, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (s[which]){
                            case "下载":
                                pd=new ProgressDialog(document.this);
                                pd.setTitle("下载");
                                pd.setMessage("正在下载中，请稍后");
                                pd.setMax(100);
                                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                pd.setCancelable(true);

                                final AVFile  file=  avObject.getAVFile("document");
                                file.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] bytes, AVException e) {
                                        pd.dismiss();
                                        if (e == null) {

                                            String dirName = Environment.getExternalStorageDirectory() + "/绵阳移动办公/";
                                            File mfile = new File(dirName);
                                            if (!mfile.exists()) {
                                                mfile.mkdir();
                                            }
                                            File downloadedFile = new File(dirName+avObject.getString("Name"));
                                            FileOutputStream fout = null;
                                            try {
                                                fout = new FileOutputStream(downloadedFile);
                                                fout.write(bytes);
                                                Toast.makeText(document.this,"下载成功",Toast.LENGTH_SHORT).show();
                                                fout.close();
                                            }
                                            catch (FileNotFoundException e1) {
                                                e1.printStackTrace();
                                                Log.d("saved", "文件找不到.." + e1.getMessage());
                                            } catch (IOException e1) {
                                                Log.d("saved", "文件读取异常.");
                                            }
                                        } else {
                                            Log.d("saved", "出错了" + e.getMessage());
                                        }
                                    }
                                }, new ProgressCallback() {
                                    @Override
                                    public void done(Integer integer) {
                                        pd.show();
                                        pd.setProgress(integer);
                                    }
                                });
                                break;
                            case "转移":
                                startActivity(new Intent(document.this, change_folder.class)
                                        .putExtra("id",avObject.getObjectId())
                                        .putExtra("folder",avObject.getString("Folder"))
                                        .putExtra("backFolder",backFolder));
                                update();
                                break;
                            case "共享":
                                share(avObject);
                                break;
                            case "删除":
                                AVQuery.doCloudQueryInBackground("delete from Document where objectId='"+avObject.getObjectId()+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                                    @Override
                                    public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                        if (e==null){
                                            switch (radioGroup.getCheckedRadioButtonId()){
                                                case R.id.radio_me:
                                                    update();
                                                    break;
                                                case R.id.radio_pub:
                                                    update_public(false);
                                                    break;
                                                case R.id.radio_do:
                                                    update_moban();
                                                    break;
                                            }
                                        }
                                    }
                                });

                                break;
                            default:
                                break;

                        }

                    }
                }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                String path =uri.getPath();
                treat_path(path);
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                String path = DocumentUtil.getPath(this, uri);
                treat_path(path);
            } else {
                String path = DocumentUtil.getRealPathFromURI(uri,this);
                treat_path(path);
            }
        }
    }
    public void treat_path(String path){
        String temp[] = path.split("/");
        final String name = temp[temp.length-1];
        Log.d("Sad",path);
        try {

            file = AVFile.withAbsoluteLocalPath(name, path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    pd.dismiss();
                    if (e ==null){
                        String Name = name;
                        AVObject document = new AVObject("Document");// 构建对象
                        document.put("document",file);
                        document.put("CreatedBy",AVUser.getCurrentUser().getUsername());
                        document.put("Type",FILE);
                        document.put("Folder",getfolder());
                        document.put("Name",Name);
                        document.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e ==null){
                                    Toast.makeText(document.this,"上传成功",Toast.LENGTH_SHORT).show();
                                    update();
                                }
                                else {
                                    Toast.makeText(document.this,"上传失败，请检查文件类型",Toast.LENGTH_SHORT).show();
                                    System.out.print(e.getMessage());
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(document.this,"上传失败",Toast.LENGTH_SHORT).show();
                    }
                }
            }, new ProgressCallback() {
                @Override
                public void done(Integer integer) {
                    pd.show();
                    pd.setProgress(integer);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void share(final AVObject avObject){
        android.support.v7.app.AlertDialog.Builder alterDialog = new android.support.v7.app.AlertDialog.Builder(document.this);
        alterDialog.setMessage("确认分享？");
        alterDialog.setCancelable(false);
        //确认函数
        alterDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AVObject document = new AVObject("Document");
                document.put("document",avObject.get("document"));
                document.put("CreatedBy",AVUser.getCurrentUser().getUsername());
                document.put("Type","file");
                document.put("Folder","公共文档");
                document.put("Name",avObject.get("Name"));
                document.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e ==null){
                            Toast.makeText(document.this,"分享",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.d("ssssss",e.getMessage());
                        }
                    }
                });


            }
        });
        //取消函数
        alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alterDialog.show();
    }
}