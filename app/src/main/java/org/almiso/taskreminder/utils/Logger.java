package org.almiso.taskreminder.utils;

import android.util.Log;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class Logger {

    public static boolean ENABLED = true;
    public static boolean LOG_THREAD = true;

    public static void d(String TAG, String message) {
        if (!ENABLED) {
            return;
        }
        if (LOG_THREAD) {
            Log.d("TaskReminder|" + TAG, Thread.currentThread().getName() + "| "
                    + message);
        } else {
            Log.d("TaskReminder|" + TAG, message);
        }
    }

    public static void e(String TAG, Exception e) {
        if (!ENABLED) {
            return;
        }
        Log.e("TaskReminder|" + TAG, "Exception in class " + TAG, e);
    }

    public static void e(String TAG, String message, Exception e) {
        if (!ENABLED) {
            return;
        }
        Log.e("TaskReminder|" + TAG, message, e);
    }

    public static void e(String TAG, String message, Throwable e) {
        if (!ENABLED) {
            return;
        }
        Log.e("TaskReminder|" + TAG, message, e);
    }

    public static void w(String TAG, String message) {
        if (LOG_THREAD) {
            Log.w("TaskReminder|" + TAG, Thread.currentThread().getName() + "| "
                    + message);
        } else {
            Log.w("TaskReminder|" + TAG, message);
        }
    }
}
