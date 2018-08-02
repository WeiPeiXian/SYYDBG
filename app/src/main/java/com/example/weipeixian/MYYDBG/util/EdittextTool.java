package com.example.weipeixian.MYYDBG.util;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class EdittextTool {
    public static void cancle(final View loutSoft, final Context context, final EditText editText){
        loutSoft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                loutSoft.setFocusable(true);
                loutSoft.setFocusableInTouchMode(true);
                loutSoft.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                return false;
            }
        });
    }
}
