package com.example.weipeixian.MYYDBG.ui.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.weipeixian.MYYDBG.BaseActivity;
import com.example.weipeixian.MYYDBG.BaseFragment;
import com.example.weipeixian.MYYDBG.R;
import com.example.weipeixian.MYYDBG.ui.activity.send_message;

public class FragmentChat extends BaseFragment {
    private Toolbar toolbar;
    private final int BACK_INTERVAL = 1000;
    private View view;
    private boolean ShowKeyboard =false;
    private static TextView back;
    private EditText ed;
    private ImageButton send;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.inner_message, container, false);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        back =view.findViewById(R.id.back);
        ed = view.findViewById(R.id.search);
        FrameLayout frameLayout = view.findViewById(R.id.framelay);
        cancle(frameLayout,getActivity(),ed);
        cancle(view,getActivity(),ed);
        send = view.findViewById(R.id.add);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),send_message.class));
            }
        });
        ed.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                back.setVisibility(View.VISIBLE);
                back.setClickable(true);
                return false;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.setVisibility(View.GONE);
                back.setClickable(false);
            }
        });
        cancle(toolbar,getActivity(),ed);
        view.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        toolbar.inflateMenu(R.menu.document);
        toolbar.setTitle("内部短信");
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        return view;
    }
    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            // 应用可以显示的区域。此处包括应用占用的区域，包括标题栏不包括状态栏
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            // 键盘最小高度
            int minKeyboardHeight = 150;
            // 屏幕高度,不含虚拟按键的高度
            int screenHeight = view.getRootView().getHeight();
            // 获取状态栏高度
            int statusBarHeight = r.top;
            // 在不显示软键盘时，height等于状态栏的高度
            int height = screenHeight - (r.bottom - r.top);
            //Log.i("wyy", "onGlobalLayout: height = " + height + ", staheight  =" + statusBarHeight + ", r.top = " + r.top + ", r.bottom = " + r.bottom);
            if (ShowKeyboard) {
                // 如果软键盘是弹出的状态，并且height小于等于状态栏高度，
                // 说明这时软键盘已经收起
                if (height - statusBarHeight < minKeyboardHeight) {
                    //ToastUtils.showToast(GoodsAllStockActivity.this, "键盘隐藏了");
                    back.setVisibility(View.GONE);
                    back.setClickable(false);
                    ed.setText("");
                    view.setFocusable(true);
                    view.setFocusableInTouchMode(true);
                    view.requestFocus();
                    ShowKeyboard = false;
                }
            } else {
                if (height - statusBarHeight > minKeyboardHeight) {
                    ShowKeyboard = true;
                }
            }
        }
    };
    public static void cancle(final View loutSoft, final Context context, final EditText editText){
        loutSoft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loutSoft.setFocusable(true);
                loutSoft.setFocusableInTouchMode(true);
                loutSoft.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.setText("");
                back.setVisibility(View.GONE);
                back.setClickable(false);
                return false;
            }
        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }



}