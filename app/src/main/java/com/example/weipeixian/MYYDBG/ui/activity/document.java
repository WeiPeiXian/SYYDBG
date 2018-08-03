package com.example.weipeixian.MYYDBG.ui.activity;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
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
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.adapter.documentAdapter;
import com.example.weipeixian.MYYDBG.util.DocumentUtil;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        pd=new ProgressDialog(document.this);
        pd.setTitle("上传");
        pd.setMessage("正在上传中，请稍后");
        pd.setMax(100);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(true);
        recyclerView = (RecyclerView)findViewById(R.id.doc_recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        itemAdapter = new documentAdapter(document.this,mlist);
        update();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio_me:
                        setTitle("个人文档");
                        invalidateOptionsMenu();
                        update();
                        ;//个人文档的操作
                        break;
                    case R.id.radio_pub:
                        setTitle("公共文档");
                        invalidateOptionsMenu();
                        update_public(true);
                        ;//公共文档的管理
                        break;

                    case R.id.radio_do:
                        setTitle("文档模板");
                        update_moban();
                        invalidateOptionsMenu();
                        ;//文档模板
                        break;

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.document, menu);//这里是调用menu文件夹中的main.xml，在登陆界面label右上角的三角里显示其他功能
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        switch (radioGroup.getCheckedRadioButtonId()){
//            case R.id.radio_me:
//                menu.findItem(R.id.upload).setVisible(true);
//                menu.findItem(R.id.add).setVisible(true);
//                menu.findItem(R.id.other).setVisible(false);
//                ; //个人文档的操作
//                break;
//
//            case R.id.radio_pub:
//                menu.findItem(R.id.other).setVisible(true);
//                menu.findItem(R.id.upload).setVisible(false);
//                menu.findItem(R.id.add).setVisible(false);
//                ;//公共文档的管理
//                break;
//            case R.id.radio_do:
//
//                menu.findItem(R.id.other).setVisible(false);
//                menu.findItem(R.id.upload).setVisible(false);
//                menu.findItem(R.id.add).setVisible(false);
//                ;//文档模板
//                break;
//        }
////        getfolder();//判断是不是个人分享的文件夹//增加分享人，增加文件夹等等//设置
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String name = getTitle().toString();
        if(name.equals("个人文档")||name.equals("公共文档")||name.equals("文档模板"))
            finish();
        else{
            setTitle(backFolder);
            update();
        }
        System.out.println("按下了back键   onBackPressed()");
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
                //个人页面的逻辑
            }
            return true;
        }
        if (menuId == R.id.upload) {
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
        //分别显示我的分享
        //t = 0 公共文档页面
        //t = 1 我的分享等等
        //t = 2 别人的分享文件夹
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
                            edit_my_file(avObject);
                            //弹出操作
                            return false;
                        }
                    });
                }

            }
        });
        //分享给我的文档
    }




    public void update_moban(){
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
                                setBackFolder();
                                setTitle(avObject.getString("Name"));
                                update_moban();
                            }
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AVObject avObject =  mlist.get(position);
                            edit_my_file(avObject);
                            //弹出操作
                            return false;
                        }
                    });
                }

            }
        });
        //判断是不是管理员//如果是管理员 有几个弹出操作
        //如果不是管理员//有什么操作

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
                                setBackFolder();
                                setTitle(avObject.getString("Name"));
                                update();
                            }
                        }
                    });
                    itemAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            AVObject avObject =  mlist.get(position);
                            edit_my_file(avObject);
                            //弹出操作
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
    public void edit_pub_file(){
    }
    public void edit_mb_file(){
    }


    public void edit_my_file(final AVObject avObject){
        //分情况//讨论
        AlertDialog dialog = new AlertDialog.Builder(document.this)
        .setTitle("操作").setItems(new String[]{"下载", "转移","分享","删除"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        final AVFile  file=  avObject.getAVFile("document");
                        file.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] bytes, AVException e) {
                                pd.dismiss();
                                if (e == null) {
                                    Log.d("saved", "文件大小" + bytes.length);
                                    File downloadedFile = new File(Environment.getExternalStorageDirectory() + "/"+file.getName());
                                    FileOutputStream fout = null;
                                    try {
                                        fout = new FileOutputStream(downloadedFile);
                                        fout.write(bytes);
                                        Log.d("saved", "文件写入成功.");
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
                    case 1:
                        //转移
                        update();
                        break;
                    case 2:
                        //分享
                        startActivity(new Intent(document.this,shareDocument.class));
                        update();
                        break;
                    case 3:
                        //删除
                        //
                        AVQuery.doCloudQueryInBackground("delete from Document where objectId='"+avObject.getObjectId()+"'", new CloudQueryCallback<AVCloudQueryResult>() {
                            @Override
                            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                                // 如果 e 为空，说明保存成功
                            }
                        });
                        switch (radioGroup.getCheckedRadioButtonId()){
                            case R.id.radio_me:
                                update();
                                ; //个人文档的操作
                                break;
                            case R.id.radio_pub:
                                update_public(false);
                                break;
                            case R.id.radio_do:
                                update_moban();
                                break;
                        }
                        update_public(false);
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
}

