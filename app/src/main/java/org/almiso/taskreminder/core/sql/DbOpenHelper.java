package org.almiso.taskreminder.core.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alexandr Sosorev on 06.04.2016.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String CREATE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TasksDatabase.TABLE_TASKS
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, tasks_array TEXT);";
        sqLiteDatabase.execSQL(CREATE_TASKS_TABLE);

        final String CREATE_ACTIVE_TASKS_TABLE = "CREATE TABLE IF NOT EXISTS " + TasksDatabase.TABLE_ACTIVE_TASKS
                + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, task_id INTEGER);";
        sqLiteDatabase.execSQL(CREATE_ACTIVE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TasksDatabase.DB_NAME);
        onCreate(sqLiteDatabase);
    }
}
