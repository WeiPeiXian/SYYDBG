package com.example.weipeixian.MYYDBG.util;
import android.content.Context;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.example.weipeixian.MYYDBG.R;


public class XmTools {

    public static void showExitDialog(final Context context) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context)
                .title("应用提示")
                .theme(Theme.LIGHT)
                .content("确定退出" + context.getResources().getString(R.string.app_name) + "吗?")
                .positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                        AppManager.getInstance().AppExit(context);
                    }
                })
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }
}