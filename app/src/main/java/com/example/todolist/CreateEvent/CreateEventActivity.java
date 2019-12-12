package com.example.todolist.CreateEvent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.todolist.*;
import com.example.todolist.DataBase.DBHelper;
import com.example.todolist.DataBase.DataBaseContract;

public class CreateEventActivity extends AppCompatActivity{

    private EditText title;
    private EditText description;
    private TextView dateOfEvent;
    private TextView timeOfEvent;
    private TextView locationOfEvent;
    private CheckBox date_event;
    private CheckBox time_event;
    private CheckBox location_event;

    private DBHelper admin;
    private SQLiteDatabase readDB, writeDB;

    private SharedPreferences shared;
    private final String SAVED_TITLE = "title";
    private final String SAVED_DESCRIPTION = "description";
    private final String SAVED_DATE = "date";
    private final String SAVED_TIME = "time";
    private static final int NOTIFICATION_ID = 1;
    public static final String CHANNEL_ID ="01";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dateOfEvent =findViewById(R.id.textViewDate);
        timeOfEvent = findViewById(R.id.textViewTime);
        locationOfEvent = findViewById(R.id.textViewLocation);
        time_event = findViewById(R.id.checkSetTime);
        date_event = findViewById(R.id.checkDate);
        description = findViewById(R.id.description_id);
        title = findViewById(R.id.title_id);
        location_event = findViewById(R.id.checkMap);

        //create SQLite db
        admin = new DBHelper(this);
        readDB = admin.getReadableDatabase();
        writeDB = admin.getWritableDatabase();

        Intent i = getIntent();

       if( i.getBooleanExtra("a",true)) {
           final String messageDate = i.getStringExtra(CalendarActivity.KEY_DATE_EVENT);
           dateOfEvent.setText(messageDate);
           date_event.setChecked(i.getBooleanExtra(CalendarActivity.IS_CHECKBOX_ACTIVE, false));
           if (date_event.isChecked())
               dateOfEvent.setVisibility(View.VISIBLE);
       }
       else {
           final String messageLocation = i.getStringExtra(MapActivity.KEY_LOCATION_EVENT);
           locationOfEvent.setText(messageLocation);
           location_event.setChecked(i.getBooleanExtra(MapActivity.IS_CHECKBOX_ACTIVE, false));
           if (location_event.isChecked())
               locationOfEvent.setVisibility(View.VISIBLE);
       }

       if (shared != null)
            loadPreferences();

    }

    //method for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void pickDate(View view) {

        if (date_event.isChecked()) {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(getSupportFragmentManager(), "Date picker: ");
            dateOfEvent.setVisibility(View.VISIBLE);
        }
        else {
            dateOfEvent.setVisibility(View.GONE);
        }

    }

    public void processDatePickerResult(int year, int month, int day){
        String month_string = Integer.toString(month+1);
        String day_string = Integer.toString(day);
        String year_string = Integer.toString(year);
        String dateMessage = (month_string + "/"+day_string+"/"+year_string);
        dateOfEvent.setText(dateMessage);
    }

    public void pickTime(View view) {
        if (time_event.isChecked()) {
            DialogFragment newFragment = new TimePickerFragment();
            newFragment.show(getSupportFragmentManager(), "Time picker: ");
            timeOfEvent.setVisibility(View.VISIBLE);
        }
        else {
            timeOfEvent.setVisibility(View.GONE);
        }

    }

    public void processTimePickerResult (int hourOfDay, int minute) {
        String hour_string = Integer.toString(hourOfDay);
        String minute_string = Integer.toString(minute);
        String timeMessage = (hour_string + " : " + minute_string);
        timeOfEvent.setText(timeMessage);
    }

    public void pickLocation(View view) {

        savePreferences();

        if (location_event.isChecked()) {
            Intent i = new Intent(this, MapCompactActivity.class);
            startActivity(i);
        }
        else {
            timeOfEvent.setVisibility(View.GONE);
        }

    }

    public void chooseMap(View view) {

    }

    public void saveTask(View view) {

        String date = dateOfEvent.getText().toString();
        String time = timeOfEvent.getText().toString();
        String desc = description.getText().toString();
        String tit = title.getText().toString();
        String place = locationOfEvent.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DataBaseContract.TasksTable.DATE_FIELD, date);
        values.put(DataBaseContract.TasksTable.TIME_FIELD, time);
        values.put(DataBaseContract.TasksTable.DESC_FIELD, desc);
        values.put(DataBaseContract.TasksTable.TITLE_FIELD, tit);
        values.put(DataBaseContract.TasksTable.PLACE_FIELD, place);

        long rowId = writeDB.insert(DataBaseContract.TasksTable.TASKS_TABLE_NAME, null, values);

        Toast.makeText(CreateEventActivity.this, rowId+ " ", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    public void savePreferences(){

        shared = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(SAVED_TITLE, title.getText().toString());
        editor.putString(SAVED_DESCRIPTION, description.getText().toString());
        editor.putString(SAVED_DATE, dateOfEvent.getText().toString());
        editor.putString(SAVED_TIME, timeOfEvent.getText().toString());
        editor.commit();

    }

    public void loadPreferences(){

        shared = getPreferences(MODE_PRIVATE);
        String savedTitle = shared.getString(SAVED_TITLE, "");
        String savedDescription = shared.getString(SAVED_DESCRIPTION, "");
        String savedDate = shared.getString(SAVED_DATE, "");
        String savedTime = shared.getString(SAVED_TIME, "");

        title.setText(savedTitle);
        description.setText(savedDescription);
        dateOfEvent.setText(savedDate);
        timeOfEvent.setText(savedTime);
    }
    public void createNotif(View view)
    {

        if (view.isEnabled())

        {
            NotificationCompat.Builder notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_stat_event)
                    .setContentTitle(title.getText().toString())
                    .setContentText(description.getText().toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis());
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(001,notification.build());
        }
        
    }

}