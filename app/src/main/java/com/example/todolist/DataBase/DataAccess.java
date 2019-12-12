package com.example.todolist.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.Tasks.Tasks;

import java.util.ArrayList;

//класс для получения данных из таблицы
public class DataAccess {
    private DBHelper admin;
    private SQLiteDatabase readDB, writeDB;
    private Context context;

    public DataAccess(Context context) {
        this.context = context;
        admin = new DBHelper(context);
        readDB = admin.getReadableDatabase();
        writeDB = admin.getWritableDatabase();
    }

    public ArrayList<Tasks> getTasks() {// квери титла и дескрипшиона в эррэй лист

        ArrayList<Tasks> list = new ArrayList<>();

        String[] projection = {DataBaseContract.TasksTable.ID_FIELD, DataBaseContract.TasksTable.TITLE_FIELD,
                DataBaseContract.TasksTable.DESC_FIELD};
        // How you want the results sorted in the resulting Curso
        //String sortOrder =MyDatabaseContract .TableWorkers.COLUMN_NAME_LAST_NAME + " DESC";
        Cursor c = readDB.query(
                DataBaseContract.TasksTable.TASKS_TABLE_NAME,  // The table to query
                projection,                                    // The columns to return
                null,                                  // The columns for the WHERE clause, all if null
                null,                                // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                    // don't filter by row groups
                null);                                  // The sort order
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) {
            list.add(new Tasks(c.getString(0), c.getString(1), c.getString(2)));
            c.moveToNext();
        }
            return list;
    }

    public Tasks getTask(String id) {// квери таска через Id

        String selection = DataBaseContract.TasksTable.ID_FIELD + " LIKE ?";

        String[] selectionArgs = { id };

        String[] projection = {DataBaseContract.TasksTable.ID_FIELD,
                DataBaseContract.TasksTable.TITLE_FIELD,
                DataBaseContract.TasksTable.DESC_FIELD,
                DataBaseContract.TasksTable.DATE_FIELD,
                DataBaseContract.TasksTable.TIME_FIELD,
                DataBaseContract.TasksTable.PLACE_FIELD};
       Cursor c = readDB.query(
                DataBaseContract.TasksTable.TASKS_TABLE_NAME,  // The table to query
                projection,                                    // The columns to return
                selection,                                     // The columns for the WHERE clause, all if null
                selectionArgs,                                 // The values for the WHERE clause
                null,                                  // don't group the rows
                null,                                   // don't filter by row groups
                null);                                 // The sort order
        //  String s ="";
        c.moveToFirst();

        return new Tasks(c.getString(0), c.getString(1), c.getString(2),c.getString(3), c.getString(4), c.getString(5));

    }

    public void deleteTask(String id) {
        String selection = DataBaseContract.TasksTable.ID_FIELD + " LIKE ?";
        String[] selectionArgs = {id};
        readDB.delete(DataBaseContract.TasksTable.TASKS_TABLE_NAME, selection, selectionArgs);
    }

}
