package com.xpf.gesturelock.library.common;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by x-sir on 2019/2/14 :)
 * Function:
 */
public class AppUtil {

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    public static int[] getScreenDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        return new int[]{width, height};
    }
}
