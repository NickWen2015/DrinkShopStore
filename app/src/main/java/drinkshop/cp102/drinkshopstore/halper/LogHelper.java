package drinkshop.cp102.drinkshopstore.halper;

import android.util.Log;

/**
 * LogHelper.e("MainActivity", "------------------");
 * Log小幫手（開關設定關閉即可關閉所有Log）
 *
 **/
public class LogHelper {
    private static boolean condition = true;  //開關

    public LogHelper() { }

    public static void e(String tag, String msg) {
        if (condition)
            Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (condition)
            Log.d(tag, msg);
    }
}
