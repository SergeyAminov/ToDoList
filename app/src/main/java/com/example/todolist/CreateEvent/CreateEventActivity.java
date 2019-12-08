package com.example.todolist.CreateEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.todolist.CalendarActivity;
import com.example.todolist.DataBase.DBHelper;
import com.example.todolist.DataBase.DataBaseContract;
import com.example.todolist.R;

public class CreateEventActivity extends AppCompatActivity{

    private EditText title;
    private EditText description;
    private TextView dateOfEvent;
    private TextView timeOfEvent;
    private CheckBox time_event;
    private CheckBox location_event;
    private DBHelper admin;
    private SQLiteDatabase readDB, writeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        dateOfEvent =findViewById(R.id.textViewDate);
        timeOfEvent = findViewById(R.id.textViewTime);
        time_event = findViewById(R.id.checkSetTime);
        description = findViewById(R.id.description_id);
        title = findViewById(R.id.title_id);
        location_event = findViewById(R.id.checkMap);

        //create SQLite db
        admin = new DBHelper(this);
        readDB=admin.getReadableDatabase();
        writeDB=admin.getWritableDatabase();


        Intent i = getIntent();
        final String messageDate = i.getStringExtra(CalendarActivity.KEY_DATE_EVENT);
        dateOfEvent.setText(messageDate);
    }

    public void pickTime(View view) {
        if (time_event.isChecked())
        {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "Time picker: ");
            timeOfEvent.setVisibility(View.VISIBLE);
        }
        else
        {
            timeOfEvent.setVisibility(View.GONE);
        }

    }

    public void processTimePickerResult (int hourofDay, int minute) {
        String hour_string = Integer.toString(hourofDay);
        String minute_string = Integer.toString(minute);
        String timeMessage = (hour_string + ":"+minute_string);
        timeOfEvent.setText(timeMessage);
    }

    public void chooseMap(View view) {

    }

    public void saveTask(View view) {

        String date = dateOfEvent.getText().toString();
        String time = timeOfEvent.getText().toString();
        String desc = description.getText().toString();
        //String place =

        ContentValues values = new ContentValues();
        values.put(DataBaseContract.TasksTable.DATE_FIELD, date);
        values.put(DataBaseContract.TasksTable.TIME_FIELD, time);
        values.put(DataBaseContract.TasksTable.DESC_FIELD, desc);

        long rowId = writeDB.insert(DataBaseContract.TasksTable.TASKS_TABLE_NAME, null, values);

        Toast.makeText(CreateEventActivity.this, rowId+ " ", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, CalendarActivity.class);
        startActivity(i);

    }

}
