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
    public static String sharedPrefFile = "helloSharedPrefs";
    public static String sharedIsLoc = "is loc";
    public static String sharedLoc = "shared loc";

    private final String SAVED_TITLE = "title";
    private final String SAVED_DESCRIPTION = "description";
    private final String SAVED_DATE = "date";
    private final String SAVED_TIME = "time";
    //private final String SAVED_LOCATION = "location";
    private final String SAVED_DATE_CHECKBOX = "date_checkbox";
    private final String SAVED_TIME_CHECKBOX = "time_checkbox";
    //private final String SAVED_LOCATION_CHECKBOX = "location_checkbox";
    private final String IS_DATE_VISIBLE = "date_textbox";
    private final String IS_TIME_VISIBLE = "time_textbox";

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

        title = findViewById(R.id.title_id);
        description = findViewById(R.id.description_id);
        dateOfEvent =findViewById(R.id.textViewDate);
        timeOfEvent = findViewById(R.id.textViewTime);
        locationOfEvent = findViewById(R.id.textViewLocation);
        time_event = findViewById(R.id.checkSetTime);
        date_event = findViewById(R.id.checkDate);
        location_event = findViewById(R.id.checkMap);

        //create SQLite db
        admin = new DBHelper(this);
        readDB = admin.getReadableDatabase();
        writeDB = admin.getWritableDatabase();

        //initialize and restore preferences
        shared = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Intent i = getIntent();

        if( i.getBooleanExtra(CalendarActivity.KEYWORD_CALENDAR,true)) {
            final String messageDate = i.getStringExtra(CalendarActivity.KEY_DATE_EVENT);
            dateOfEvent.setText(messageDate);
           date_event.setChecked(i.getBooleanExtra(CalendarActivity.IS_CHECKBOX_ACTIVE, false));

            if (date_event.isChecked())
                dateOfEvent.setVisibility(View.VISIBLE);
        }

        if( i.getBooleanExtra(MapActivity.KEYWORD_MAP,true)) {



            location_event.setChecked(getSharedPreferences(sharedPrefFile,MODE_PRIVATE).getBoolean(CreateEventActivity.sharedIsLoc,false));
            if (location_event.isChecked())
                locationOfEvent.setVisibility(View.VISIBLE);

            // final String messageLocation = i.getStringExtra(MapActivity.KEY_LOCATION_EVENT);
            String messageLocation = getSharedPreferences(sharedPrefFile,MODE_PRIVATE).getString(sharedLoc,"");

            locationOfEvent.setText(messageLocation);

        }

        if( i.getBooleanExtra(MapCompactActivity.KEYWORD_MAP_COMPACT,true)) {
            final String messageLocationCompact = i.getStringExtra(MapCompactActivity.KEY_LOCATION_EVENT);
            locationOfEvent.setText(messageLocationCompact);
            location_event.setChecked(i.getBooleanExtra(MapCompactActivity.IS_CHECKBOX_ACTIVE, false));
            if (location_event.isChecked())
                locationOfEvent.setVisibility(View.VISIBLE);
        }

    }

    public void savePreferences(SharedPreferences shared) {

        SharedPreferences.Editor preferencesEditor = shared.edit();

        preferencesEditor.putString(SAVED_TITLE, title.getText().toString());
        preferencesEditor.putString(SAVED_DESCRIPTION, description.getText().toString());

        preferencesEditor.putString(SAVED_TIME, timeOfEvent.getText().toString());
        preferencesEditor.putString(SAVED_DATE, dateOfEvent.getText().toString());
        //preferencesEditor.putString(SAVED_LOCATION, locationOfEvent.getText().toString());

        preferencesEditor.putBoolean(SAVED_TIME_CHECKBOX, time_event.isChecked());
        preferencesEditor.putBoolean(SAVED_DATE_CHECKBOX, date_event.isChecked());
        //preferencesEditor.putBoolean(SAVED_LOCATION_CHECKBOX, location_event.isChecked());

        preferencesEditor.putBoolean(IS_TIME_VISIBLE, time_event.isChecked());
        preferencesEditor.putBoolean(IS_DATE_VISIBLE, date_event.isChecked());

        preferencesEditor.apply();

    }

    public void loadPreferences(SharedPreferences shared){

        title.setText(shared.getString(SAVED_TITLE, ""));
        description.setText(shared.getString(SAVED_DESCRIPTION, ""));

        timeOfEvent.setText(shared.getString(SAVED_TIME, ""));
        dateOfEvent.setText(shared.getString(SAVED_DATE, ""));
        //locationOfEvent.setText(shared.getString(SAVED_LOCATION, ""));

        time_event.setChecked(shared.getBoolean(SAVED_TIME_CHECKBOX, false));
        date_event.setChecked(shared.getBoolean(SAVED_DATE_CHECKBOX, false));
        //location_event.setChecked(shared.getBoolean(SAVED_LOCATION_CHECKBOX, false));

        if (shared.getBoolean(IS_TIME_VISIBLE, false))
            timeOfEvent.setVisibility(View.VISIBLE);
        else
            timeOfEvent.setVisibility(View.GONE);

        if (shared.getBoolean(IS_DATE_VISIBLE, false))
            dateOfEvent.setVisibility(View.VISIBLE);
        else
            dateOfEvent.setVisibility(View.GONE);


    }

    public void deletePreferences(SharedPreferences shared){

        SharedPreferences.Editor preferencesEditor = shared.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();

    }

    @Override
    public void onStart(){
        super.onStart();
        //Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onPause(){
        savePreferences(this.shared);
        super.onPause();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        //Log.d(LOG_TAG, "onRestart");
    }

    @Override
    public void onResume(){

        loadPreferences(this.shared);

        if( getIntent().getBooleanExtra(MapActivity.KEYWORD_MAP,true)) {

            location_event.setChecked(getSharedPreferences(sharedPrefFile,MODE_PRIVATE).getBoolean(CreateEventActivity.sharedIsLoc,false));
            if (location_event.isChecked())
                locationOfEvent.setVisibility(View.VISIBLE);

            // final String messageLocation = i.getStringExtra(MapActivity.KEY_LOCATION_EVENT);
            String messageLocation = getSharedPreferences(sharedPrefFile,MODE_PRIVATE).getString(sharedLoc,"");

            locationOfEvent.setText(messageLocation);

        }

        super.onResume();
        //Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onStop(){
        super.onStop();
        //Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onDestroy(){
        //deletePreferences(this.shared);
        super.onDestroy();
        //Log.d(LOG_TAG, "onDestroy");
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

        if (location_event.isChecked()) {

            savePreferences(this.shared);

            Intent i = new Intent(this, MapCompactActivity.class);
            startActivity(i);
        }
        else {

            SharedPreferences  shared = getSharedPreferences(CreateEventActivity.sharedPrefFile, MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putBoolean(CreateEventActivity.sharedIsLoc, false);
            editor.putString(CreateEventActivity.sharedLoc,"");

            editor.commit();


            locationOfEvent.setVisibility(View.GONE);
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

        deletePreferences(this.shared);

        Toast.makeText(CreateEventActivity.this, rowId+ " ", Toast.LENGTH_LONG).show();

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    public void createNotif(View view) {

        if (view.isEnabled()) {
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