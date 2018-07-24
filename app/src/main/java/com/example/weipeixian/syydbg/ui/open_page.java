package com.example.weipeixian.syydbg.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.weipeixian.syydbg.R;

public class open_page extends AppCompatActivity{
    private ListView Lv;
    private String[] data = { "短信", "通讯录", "公告", "新闻",
            "文档", "请假流程","退出" };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_page);
        Lv = (ListView) findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(open_page.this, android.R.layout.simple_list_item_1, data);
        Lv.setAdapter(adapter);
        Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(i);
                Intent[] it = new Intent[6];
                it[0] =new Intent(open_page.this,SMSHostActivity.class);
                it[1] =new Intent(open_page.this,contacts.class);
                it[2] =new Intent(open_page.this,announcement.class);
                it[3] =new Intent(open_page.this,news.class);
                it[4] =new Intent(open_page.this,document.class);
                it[5] =new Intent(open_page.this,process.class);
                if (i<6){
                    startActivity(it[i]);
                }
                else {
//                    loginout()
                }
            }
        });
    }
}
