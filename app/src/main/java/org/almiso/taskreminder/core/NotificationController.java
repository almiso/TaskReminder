package org.almiso.taskreminder.core;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.TextUtils;

import org.almiso.taskreminder.R;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.ui.activity.ActivityMain;
import org.almiso.taskreminder.utils.AndroidUtilities;
import org.almiso.taskreminder.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Alexandr Sosorev on 06.04.2016.
 */
public class NotificationController {

    private NotificationManagerCompat notificationManager = null;

    /* Base */
    private static volatile NotificationController Instance = null;

    public static NotificationController getInstance() {
        NotificationController localInstance = Instance;
        if (localInstance == null) {
            synchronized (NotificationController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new NotificationController();
                }
            }
        }
        return localInstance;
    }

    private NotificationController() {
        notificationManager = NotificationManagerCompat.from(TaskReminderApplication.applicationContext);
    }


    public void update() {
        notificationManager.cancelAll();
        int activeTasksListId = TasksController.getInstance().getActiveListId();
        if (activeTasksListId == -1) {
            return;
        }

        ArrayList<Task> array = TasksController.getInstance().getTasks(activeTasksListId);
        for (Task task : array) {
            if (task.taskState == Task.TASK_STATE_NOT_DONE && !TextUtils.isEmpty(task.value)) {
                updateTaskNotification(task, activeTasksListId);
            }
        }
    }

    private void updateTaskNotification(Task task, int activeTasksListId) {

        Intent intent = new Intent(TaskReminderApplication.applicationContext, ActivityMain.class);
        intent.setAction(Constants.ACTION_OPEN_TASKS_LIST);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constants.EXTRA_TASK_LIST_ID, activeTasksListId);
        PendingIntent contentIntent = PendingIntent.getActivity(TaskReminderApplication.applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


//        /* action */
//        Intent actionIntent = new Intent(TaskReminderApplication.applicationContext, ActivityMain.class);
//        actionIntent.setAction(Constants.ACTION_OPEN_TASKS_LIST_AND_DONE_TASK);
//        actionIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        actionIntent.putExtra(Constants.EXTRA_TASK_LIST_ID, activeTasksListId);
//        actionIntent.putExtra(Constants.EXTRA_TASK_ID, task.id);
//        PendingIntent contentIntentComment = PendingIntent.getActivity(TaskReminderApplication.applicationContext, 0, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Action action = new NotificationCompat.Action(
//                R.drawable.ic_check_grey600_24dp,
//                TaskReminderApplication.application.getResources().getString(R.string.st_done),
//                contentIntentComment);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(TaskReminderApplication.applicationContext)
                .setContentTitle(task.value)
                .setContentInfo(TasksController.getInstance().getTaskListName(activeTasksListId))
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(AndroidUtilities.getResources(), R.mipmap.ic_launcher))
//                .addAction(action)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_format_list_bulleted_white_24dp);

        notificationManager.notify(task.id, mBuilder.build());
    }
}
