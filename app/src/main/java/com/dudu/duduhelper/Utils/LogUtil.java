package com.dudu.duduhelper.Utils;

import android.util.Log;

/**
 * Created by lwz on 2016/8/24.
 */
public class LogUtil {

    static  boolean isLog = true;
    public  static void d(String tag,String content){
        if (isLog){

            Log.d(tag,content);
        }
    }
}
