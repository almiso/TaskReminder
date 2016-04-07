package org.almiso.taskreminder.core;

import org.almiso.taskreminder.core.sql.TasksDatabase;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.model.TasksList;

import java.util.ArrayList;

/**
 * Created by Alexandr Sosorev on 05.04.2016.
 */
public class TasksController {

    private TasksDatabase database;

    /* Base */
    private static volatile TasksController Instance = null;

    public static TasksController getInstance() {
        TasksController localInstance = Instance;
        if (localInstance == null) {
            synchronized (TasksController.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new TasksController();
                }
            }
        }
        return localInstance;
    }

    private TasksController() {
        database = new TasksDatabase();
    }

    /* tasks list */

    public ArrayList<TasksList> getTasksLists() {
        return database.getTasksLists();
    }


    public long addNewEmptyTasksList() {
        return database.addNewEmptyTasksList();
    }

    public String getTasksListName(int taskListId) {
        return database.getTasksListName(taskListId);
    }

    public void updateTasksListName(int taskListId, String value, boolean isTaskListActive) {
        database.updateTasksListName(taskListId, value);
        if (isTaskListActive) {
            NotificationController.getInstance().update();
        }
    }

    public void deleteTasksList(int taskListId, boolean isTaskListActive) {
        database.deleteTasksList(taskListId);
        if (isTaskListActive) {
            NotificationController.getInstance().update();
        }
    }

    /* tasks items */

    public ArrayList<Task> getTasks(int taskListId) {
        return database.getTasks(taskListId);
    }

    public void addNewEmptyTask(int taskListId) {
        database.addNewEmptyTask(taskListId);
    }

    public void onTaskValueChanged(int taskListId, int taskId, String value, boolean isTaskListActive) {
        database.onTaskValueChanged(taskListId, taskId, value);
        if (isTaskListActive) {
            NotificationController.getInstance().update();
        }
    }

    public void onTaskStateChanged(int taskListId, int taskId, @Task.TaskState int state, boolean isTaskListActive) {
        database.onTaskStateChanged(taskListId, taskId, state);
        if (isTaskListActive) {
            NotificationController.getInstance().update();
        }
    }

    public void deleteTask(int taskListId, int taskId, boolean isTaskListActive) {
        database.deleteTask(taskListId, taskId);
        if (isTaskListActive) {
            NotificationController.getInstance().update();
        }
    }

    /* other */
    public void setActiveListId(int tasksListId) {
        database.setActiveListId(tasksListId);
        NotificationController.getInstance().update();
    }

    public int getActiveListId() {
        return database.getActiveListId();
    }

    public String getTaskListName(int taskListId) {
        return database.getTaskListName(taskListId);
    }

}
