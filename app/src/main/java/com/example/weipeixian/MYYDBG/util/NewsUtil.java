package com.example.weipeixian.MYYDBG.util;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.weipeixian.MYYDBG.adapter.newsAdapter;
import com.example.weipeixian.MYYDBG.ui.activity.news;

import java.util.List;

public class NewsUtil {
    public static void add(){
        AVObject object = new AVObject("News");
        object.put("Title", "新闻列表");
        object.put("body", "新闻内容");
        object.put("time", "当前时间");
        object.put("type", "新闻类别");
        //等等等等w
        object.saveInBackground();
    }
    public static void get(){
        AVQuery<AVObject> query = new AVQuery<>("News");
        query.whereEqualTo("type","新闻类别");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e!=null){
                    System.out.print(e.getMessage());
                }
                else {
                    //往列表添加内容
                }
            }
        });
    }
}
