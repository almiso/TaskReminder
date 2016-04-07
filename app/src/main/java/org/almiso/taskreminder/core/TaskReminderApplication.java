package org.almiso.taskreminder.core;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class TaskReminderApplication extends Application {

    private static final String TAG = "TaskReminderApplication";

    public static volatile TaskReminderApplication application = null;
    public static volatile Context applicationContext = null;
    public static volatile Handler applicationHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = getApplicationContext();
        application = (TaskReminderApplication) getApplicationContext();
        applicationHandler = new Handler(applicationContext.getMainLooper());
        NotificationController.getInstance().update();
    }
}
