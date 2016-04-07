package org.almiso.taskreminder.core.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.almiso.taskreminder.core.TaskReminderApplication;
import org.almiso.taskreminder.model.Task;
import org.almiso.taskreminder.model.TasksList;
import org.almiso.taskreminder.utils.Logger;
import org.almiso.taskreminder.utils.TasksReminderUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Alexandr Sosorev on 06.04.2016.
 */
public class TasksDatabase {

    private static final String TAG = "TasksDatabase";

    public static final String DB_NAME = "tasks_bd";
    public static final int DB_VERSION = 2;
    public static final String TABLE_TASKS = "tasks_table";
    public static final String TABLE_ACTIVE_TASKS = "active_tasks_table";

    private DbOpenHelper dbOpenHelper;

    public TasksDatabase() {
        init();
    }

    private void init() {
        dbOpenHelper = new DbOpenHelper(TaskReminderApplication.applicationContext, DB_NAME, null, DB_VERSION);
    }


    /* tasks lists */

    public ArrayList<TasksList> getTasksLists() {
        ArrayList<TasksList> result = new ArrayList<>();
        SQLiteDatabase database = getDatabase();
        String[] columnsToTake = {"_id", "name", "tasks_array"};
        Cursor cursor = database.query(TABLE_TASKS, columnsToTake, null, null, null, null, "_id");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {

                int id = cursor.getInt(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String tasksArrayString = cursor.getString(cursor.getColumnIndex("tasks_array"));

                TasksList tasksList = new TasksList();
                tasksList.id = id;
                tasksList.name = name;
                tasksList.tasks = TasksReminderUtils.convertStringToArray(tasksArrayString);

                result.add(tasksList);
                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();
        return result;
    }

    public long addNewEmptyTasksList() {
        ContentValues values = new ContentValues();
        values.put("name", "");
        values.put("tasks_array", "");

        SQLiteDatabase database = getDatabase();
        long id = database.insert(TABLE_TASKS, null, values);
        database.close();
        return id;
    }

    public void updateTasksListName(int taskListId, String value) {
        SQLiteDatabase database = getDatabase();
        ContentValues values = new ContentValues();
        values.put("name", value);
        database.update(TABLE_TASKS, values, "_id=?", new String[]{String.valueOf(taskListId)});
        database.close();
    }

    public String getTasksListName(int taskListId) {
        SQLiteDatabase database = getDatabase();
        String name = "";
        String[] columnsToTake = {"name"};
        Cursor cursor = database.query(TABLE_TASKS, columnsToTake, "_id=?", new String[]{String.valueOf(taskListId)}, null, null, "_id");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();
        return name;
    }

    /* tasks items */

    public ArrayList<Task> getTasks(int taskListId) {
        ArrayList<Task> result = new ArrayList<>();
        SQLiteDatabase database = getDatabase();

        String[] columnsToTake = {"_id", "name", "tasks_array"};
        Cursor cursor = database.query(TABLE_TASKS, columnsToTake, "_id=?", new String[]{String.valueOf(taskListId)}, null, null, "_id");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String tasksArrayString = cursor.getString(cursor.getColumnIndex("tasks_array"));
                result = TasksReminderUtils.convertStringToArray(tasksArrayString);
                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();
        return result;
    }


    public void addNewEmptyTask(int taskListId) {
        ArrayList<Task> array = getTasks(taskListId);
        Task task = new Task();
        task.id = getNextTaskId(array);
        array.add(task);
        SQLiteDatabase database = getDatabase();
        ContentValues values = new ContentValues();
        values.put("tasks_array", TasksReminderUtils.convertArrayToString(array));
        database.update(TABLE_TASKS, values, "_id=?", new String[]{String.valueOf(taskListId)});
        database.close();
    }


    public void onTaskValueChanged(int taskListId, int taskId, String value) {
        ArrayList<Task> array = getTasks(taskListId);
        for (Task task : array) {
            if (task.id == taskId) {
                task.value = value;
            }
        }
        SQLiteDatabase database = getDatabase();
        ContentValues values = new ContentValues();
        values.put("tasks_array", TasksReminderUtils.convertArrayToString(array));
        database.update(TABLE_TASKS, values, "_id=?", new String[]{String.valueOf(taskListId)});
        database.close();
    }

    public void onTaskStateChanged(int taskListId, int taskId, @Task.TaskState int state) {
        ArrayList<Task> array = getTasks(taskListId);
        for (Task task : array) {
            if (task.id == taskId) {
                task.taskState = state;
            }
        }
        SQLiteDatabase database = getDatabase();
        ContentValues values = new ContentValues();
        values.put("tasks_array", TasksReminderUtils.convertArrayToString(array));
        database.update(TABLE_TASKS, values, "_id=?", new String[]{String.valueOf(taskListId)});
        database.close();
    }

    public boolean deleteTasksList(int taskListId) {
        SQLiteDatabase database = getDatabase();
        String[] columnsToTake = {"_id"};
        Cursor cursor = database.query(TABLE_TASKS, columnsToTake, "_id=?", new String[]{String.valueOf(taskListId)}, null, null, "_id");

        if (cursor.getCount() == 0) {
            Logger.d(TAG, "No rows for list id " + taskListId);
            cursor.close();
            database.close();
            return false;
        }

        Logger.d(TAG, String.format(Locale.getDefault(), "Deleting list with id=%d, count=%d", taskListId, cursor.getCount()));
        database.delete(TABLE_TASKS, "_id=?", new String[]{String.valueOf(taskListId)});
        cursor.close();
        database.close();
        return true;
    }

    public boolean deleteTask(int taskListId, int taskId) {
        SQLiteDatabase database = getDatabase();

        String[] columnsToTake = {"_id", "tasks_array"};
        Cursor cursor = database.query(TABLE_TASKS, columnsToTake, "_id=?", new String[]{String.valueOf(taskListId)}, null, null, "_id");

        ArrayList<Task> array = new ArrayList<>();

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String tasksArrayString = cursor.getString(cursor.getColumnIndex("tasks_array"));
                array = TasksReminderUtils.convertStringToArray(tasksArrayString);
                cursor.moveToNext();
            }
        }

        Iterator<Task> iterator = array.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (task.id == taskId) {
                iterator.remove();
            }
        }

        ContentValues values = new ContentValues();
        values.put("tasks_array", TasksReminderUtils.convertArrayToString(array));
        database.update(TABLE_TASKS, values, "_id=?", new String[]{String.valueOf(taskListId)});
        database.close();
        return true;
    }

    public void setActiveListId(int tasksListId) {
        SQLiteDatabase database = getDatabase();

        database.delete(TABLE_ACTIVE_TASKS, "1", null);
        ContentValues values = new ContentValues();
        values.put("task_id", tasksListId);
        database.insert(TABLE_ACTIVE_TASKS, null, values);
        database.close();
    }

    public String getTaskListName(int taskListId) {
        String name = "";

        SQLiteDatabase database = getDatabase();
        String[] columnsToTake = {"name"};
        Cursor cursor = database.query(TABLE_TASKS, columnsToTake, "_id=?", new String[]{String.valueOf(taskListId)}, null, null, "_id");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();
        return name;
    }

    public int getActiveListId() {
        int id = -1;
        SQLiteDatabase database = getDatabase();

        String[] columnsToTake = {"_id", "task_id"};
        Cursor cursor = database.query(TABLE_ACTIVE_TASKS, columnsToTake, null, null, null, null, "_id");

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                id = cursor.getInt(cursor.getColumnIndex("task_id"));
                cursor.moveToNext();
            }
        }

        cursor.close();
        database.close();
        return id;
    }

    /* private methods */

    private int getNextTaskId(ArrayList<Task> array) {
        int id = -1;

        for (Task task : array) {
            if (task.id > id) {
                id = task.id;
            }
        }
        return id + 1;
    }

    private SQLiteDatabase getDatabase() {
        SQLiteDatabase database;
        try {
            database = dbOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            Logger.e(TAG, "Error while getting database", e);
            throw new Error("The end");
        }
        return database;
    }
}
