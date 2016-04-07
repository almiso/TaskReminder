package org.almiso.taskreminder.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class Task {

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TASK_STATE_NOT_DONE, TASK_STATE_DONE})
    public @interface TaskState {
    }

    public static final int TASK_STATE_NOT_DONE = 0;
    public static final int TASK_STATE_DONE = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({PRIORITY_SMALL, PRIORITY_NORMAL, PRIORITY_HIGH})
    public @interface Priority {
    }

    public static final int PRIORITY_SMALL = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    /* fields */

    public int id = 0;
    @TaskState
    public int taskState = TASK_STATE_NOT_DONE;
    public String value = "";
    @Priority
    public int priority = PRIORITY_SMALL;
}
