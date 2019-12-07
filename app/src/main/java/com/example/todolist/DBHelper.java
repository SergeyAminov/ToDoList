package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = DataBaseContract.DB_NAME;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";

    private static final String SQL_CREATE_TASK =
    "CREATE TABLE " + DataBaseContract.TasksTable.TASKS_TABLE_NAME  + " (" +
    DataBaseContract.TasksTable.ID_FIELD + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
    DataBaseContract.TasksTable.DATE_FIELD + TEXT_TYPE + COMMA_SEP +
    DataBaseContract.TasksTable.DESC_FIELD + TEXT_TYPE + COMMA_SEP +
    DataBaseContract.TasksTable.PLACE_FIELD + TEXT_TYPE + COMMA_SEP +
    DataBaseContract.TasksTable.TIME_FIELD + TEXT_TYPE +                 " )";

    private static final String SQL_DELETE_TASK =
            "DROP TABLE IF EXISTS " + DataBaseContract.TasksTable.TASKS_TABLE_NAME;




    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(SQL_CREATE_TASK);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_TASK);
        onCreate(db);

    }
}
