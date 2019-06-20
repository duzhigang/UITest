package com.ggec.uitest.utils;

import android.content.res.Resources;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.view.Display;

public class ScreenUtil {
    private static Point point = new Point();

    /**
     * 获取屏幕宽度
     *
     * @param activity FragmentActivity
     * @return ScreenWidth
     */
    public static int getScreenWidth(FragmentActivity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (display != null) {
            display.getSize(point);
            return point.x;
        }
        return 0;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity FragmentActivity
     * @return ScreenHeight
     */
    public static int getScreenHeight(FragmentActivity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (display != null) {
            display.getSize(point);
            return point.y - getStatusBarHeight(activity);
        }
        return 0;
    }

    /**
     * 计算statusBar高度
     *
     * @param activity FragmentActivity
     * @return statusBar高度
     */
    public static int getStatusBarHeight(FragmentActivity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 计算navigationBar高度
     *
     * @param activity FragmentActivity
     * @return navigationBar高度
     */
    public static int getNavigationBarHeight(FragmentActivity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
