package com.young.note.util;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class AlertDialogUtil {
    public static void showDialog(Context context, String msg, DialogInterface.OnClickListener positiveListener) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定", positiveListener)
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
    }

    public static AlertDialog showDialog(Context context, View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(view);
        builder.setCancelable(false);
        return builder.create();
    }
}
