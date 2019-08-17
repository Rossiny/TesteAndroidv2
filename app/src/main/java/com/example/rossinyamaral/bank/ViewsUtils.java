package com.example.rossinyamaral.bank;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Window;
import android.view.WindowManager;

public class ViewsUtils {
    public static final String TAG = ViewsUtils.class.getSimpleName();

    private static ProgressDialog progress;

    public static void loading(Context context) {
        try {
            dismissLoading();
            progress = ProgressDialog.show(
                    new ContextThemeWrapper(context, R.style.LoadingStyle),
                    "", "Carregando...", true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public static void dismissLoading() {
        try {
            if (progress != null) {
                progress.dismiss();
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            progress = null;
        }
    }

    public static void alert(Context context, String message, DialogInterface.OnDismissListener listener) {
        new AlertDialog.Builder(context, R.style.LoadingStyle)
                .setMessage(message)
                .setCancelable(false)
                .setOnDismissListener(listener)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, color));
        }
    }
}
