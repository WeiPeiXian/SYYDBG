package com.example.weipeixian.MYYDBG;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import java.util.logging.Handler;

public class BaseFragment extends Fragment {

    protected BaseApplication mApplication;
    protected Context mContext;
    protected android.os.Handler mHandler;
    protected String TAG;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mApplication = (BaseApplication) ((Activity)context).getApplication();
        TAG=this.getClass().getSimpleName();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}