package com.example.weipeixian.MYYDBG.ui.activity.news;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.ProgressCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.util.DocumentUtil;

import java.io.FileNotFoundException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class newschange extends AppCompatActivity {
    private ProgressDialog pd;
    AVFile file = null;
    @BindView(R.id.catalog)
    LinearLayout m;
    private EditText titleEdit;
    private EditText contentEdit;
    private Spinner spinner;
    private String leibie;
    private CheckBox  box1,box2;
    private String id;
    private boolean zhiding =false;
    private boolean pinglun =false;
    @BindView(R.id.return5)
    ImageButton back;
    @BindView(R.id.title)
    TextView title;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_news );

        titleEdit= (EditText)findViewById(R.id.news_title);
        contentEdit= (EditText)findViewById(R.id.news );
        spinner = (Spinner) findViewById(R.id.spinner);
        box1 = findViewById(R.id.zhiding);
        box2 = findViewById(R.id.pinglun);
        submit=findViewById(R.id.sub);
        id = this.getIntent().getStringExtra("id");
        ButterKnife.bind(this);
        title.setText("修改新闻");
        final SpinnerAdapter apsAdapter= spinner.getAdapter();
        final int k= apsAdapter.getCount();
        AVQuery<AVObject> query =new AVQuery<>("News");
        query.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e ==null){
                    titleEdit.setText(avObject.getString("title"));
                    contentEdit.setText(avObject.getString("content"));
                    box1.setChecked(avObject.getBoolean("zhiding"));
                    box2.setChecked(avObject.getBoolean("pinglun"));
                    file = avObject.getAVFile("picture");
                    for(int i=0;i<k;i++){
                        if(avObject.getString("type").equals(apsAdapter.getItem(i).toString())){
                            spinner.setSelection(i);
                            break;
                        }
                    }

                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                leibie =apsAdapter.getItem(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd=new ProgressDialog(newschange.this);
                pd.setTitle("上传");
                pd.setMessage("正在上传中，请稍后");
                pd.setMax(100);
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setCancelable(true);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");//选择图片
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
        box1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    zhiding=isChecked;
            }
        });
        box2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    pinglun=isChecked;

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEdit.getText().toString();
                String content = contentEdit.getText().toString();
                AVObject avObject = AVObject.createWithoutData("News", id);
                avObject.put("title",title);
                avObject.put("content",content);
                avObject.put("type",leibie);
                avObject.put("zhiding",zhiding);
                avObject.put("pinglun",pinglun);
                avObject.put("picture",file);
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if(e==null){
                            finish();
                        }
                        else {
                        }
                    }
                });

            }
        });

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
                        Toast.makeText(newschange.this,"上传成功",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(newschange.this,"上传失败",Toast.LENGTH_SHORT).show();
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


