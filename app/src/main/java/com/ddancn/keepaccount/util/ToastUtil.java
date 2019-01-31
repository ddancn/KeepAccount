package com.ddancn.keepaccount.util;

import android.app.Application;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {

    private static Toast sToast;
    private static Application sApplication;

    public static void init(Application app){
        sApplication = app;
    }

    public static void show(CharSequence msg){
        if (msg == null || msg.equals("")) return;
        if(sToast == null){
            sToast = Toast.makeText(sApplication, msg, Toast.LENGTH_SHORT);
        }
        else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void show(int resId){
        try{
            if(sToast == null) {
                sToast = Toast.makeText(sApplication, resId, Toast.LENGTH_SHORT);
            }else {
                sToast.setText(resId);
            }
        } catch (Resources.NotFoundException e){
            show(String.valueOf(resId));
        }
        sToast.show();
    }

    public static void showOnUIThread(final CharSequence msg){
        new Handler(Looper.getMainLooper()).post(() -> show(msg));
    }

    public static void showOnUIThread(final int resId){
        new Handler(Looper.getMainLooper()).post(() -> show(resId));
    }

    public static void cancel(){
        if(sToast != null) {
            sToast.cancel();
        }
    }

    public static Toast getToast(){
        return sToast;
    }

}
