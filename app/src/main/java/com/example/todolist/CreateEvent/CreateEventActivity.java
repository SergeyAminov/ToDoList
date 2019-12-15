package com.example.todolist.CreateEvent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static java.util.Calendar.DAY_OF_MONTH;

public class CreateEventActivity extends AppCompatActivity{

    public static EditText title;
    public static EditText description;
    private TextView dateOfEvent;
    private TextView timeOfEvent;
    private TextView locationOfEvent;
    private CheckBox date_event;
    private CheckBox time_event;
    private CheckBox location_event;
    Switch createReminder;
    Button setDate;
    Button setTime;
    TextView textViewDateReminder;
    TextView textViewTimeReminder;
    Button setNotification;

    private DBHelper admin;
    private SQLiteDatabase readDB, writeDB;

    private SharedPreferences shared;
    private String sharedPrefFile = "helloSharedPrefs";
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
        setDate = findViewById(R.id.btnSetDate);
        setTime = findViewById(R.id.btnSetTime);
        createReminder = findViewById(R.id.switchSetNotification);
        textViewDateReminder = findViewById(R.id.dateReminder);
        textViewTimeReminder = findViewById(R.id.timeReminder);
        setNotification = findViewById(R.id.btnSetNotification);

        //create SQLite db
        admin = new DBHelper(this);
        readDB = admin.getReadableDatabase();
        writeDB = admin.getWritableDatabase();

        //initialize and restore preferences
        shared = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        Intent i = getIntent();

        loadPreferences(this.shared);

        if( i.getBooleanExtra("a",true)) {
            final String messageDate = i.getStringExtra(CalendarActivity.KEY_DATE_EVENT);
            dateOfEvent.setText(messageDate);
            date_event.setChecked(i.getBooleanExtra(CalendarActivity.IS_CHECKBOX_ACTIVE, false));
            if (date_event.isChecked())
                dateOfEvent.setVisibility(View.VISIBLE);
        }

        else if( i.getBooleanExtra("b",true)) {
            final String messageLocation = i.getStringExtra(MapActivity.KEY_LOCATION_EVENT);
            locationOfEvent.setText(messageLocation);
            location_event.setChecked(i.getBooleanExtra(MapActivity.IS_CHECKBOX_ACTIVE, false));
            if (location_event.isChecked())
                locationOfEvent.setVisibility(View.VISIBLE);
        }
        /*
        else if( i.getBooleanExtra("c",true)) {
            final String messageLocation = i.getStringExtra(MapCompactActivity.KEY_LOCATION_EVENT);
            locationOfEvent.setText(messageLocation);
            location_event.setChecked(i.getBooleanExtra(MapCompactActivity.IS_CHECKBOX_ACTIVE, false));
            if (location_event.isChecked())
                locationOfEvent.setVisibility(View.VISIBLE);
        }
        */
        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                if(calendar1.compareTo(currentTime) <= 0){
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(), "Choose your Date/Time", Toast.LENGTH_LONG).show();
                }else{
                    setAlarm(calendar1);
                }


            }
        });

        savePreferences(this.shared);

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
        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                if(calendar1.compareTo(currentTime) <= 0){

                    Toast.makeText(getApplicationContext(), "Choose your Date/Time", Toast.LENGTH_LONG).show();
                }
                else{
                    setAlarm(calendar1);
                }
            }

        });
        super.onPause();
    }

    @Override
    public void onRestart(){
        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                if(calendar1.compareTo(currentTime) <= 0){

                    Toast.makeText(getApplicationContext(), "Choose your Date/Time", Toast.LENGTH_LONG).show();
                }
                else{
                    setAlarm(calendar1);
                }
            }

        });
        super.onRestart();
        //Log.d(LOG_TAG, "onRestart");
    }

    @Override
    public void onResume(){
        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                if(calendar1.compareTo(currentTime) <= 0){

                    Toast.makeText(getApplicationContext(), "Choose your Date/Time", Toast.LENGTH_LONG).show();
                }
                else{
                    setAlarm(calendar1);
                }
            }

        });

        loadPreferences(this.shared);

        super.onResume();
        //Log.d(LOG_TAG, "onResume");
    }

    @Override
    public void onStop(){
        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                if(calendar1.compareTo(currentTime) <= 0){

                    Toast.makeText(getApplicationContext(), "Choose your Date/Time", Toast.LENGTH_LONG).show();
                }
                else{
                    setAlarm(calendar1);
                }
            }

        });
        super.onStop();
        //Log.d(LOG_TAG, "onStop");
    }

    @Override
    public void onDestroy(){
        setNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                if(calendar1.compareTo(currentTime) <= 0){

                    Toast.makeText(getApplicationContext(), "Choose your Date/Time", Toast.LENGTH_LONG).show();
                }
                else{
                    setAlarm(calendar1);
                }
            }

        });
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

    Calendar calendar1 = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, monthOfYear);
            calendar1.set(DAY_OF_MONTH, dayOfMonth);
            String formatYEAR = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(formatYEAR, Locale.US);
            textViewDateReminder.setText("Date Reminder: " + sdf.format(calendar1.getTime()));

        }
    };
    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            calendar1.set(Calendar.HOUR, hourOfDay);
            calendar1.set(Calendar.MINUTE, minute);
            textViewTimeReminder.setText("Time reminder: "+ hourOfDay+":"+minute);
        }
    };


    public void createNotif(View view) {
        if (createReminder.isChecked()) {
            setDate.setVisibility(View.VISIBLE);
            setDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new DatePickerDialog(CreateEventActivity.this, date, calendar1
                            .get(Calendar.YEAR), calendar1.get(Calendar.MONTH),
                            calendar1.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            textViewDateReminder.setVisibility(View.VISIBLE);
            setTime.setVisibility(View.VISIBLE);
            setNotification.setVisibility(View.VISIBLE);

            setTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TimePickerDialog(CreateEventActivity.this, time,calendar1.get(Calendar.HOUR)
                            ,calendar1.get(Calendar.MINUTE),true).show();
                }
            });
            textViewTimeReminder.setVisibility(View.VISIBLE);

        } else {
            setDate.setVisibility(View.GONE);
            setTime.setVisibility(View.GONE);
            textViewDateReminder.setVisibility(View.GONE);
            textViewDateReminder.setVisibility(View.GONE);
            setNotification.setVisibility(View.GONE);
        }
    }


    private void setAlarm (Calendar reminderCal)
    {
        Intent intent = new Intent(getBaseContext(), AlarmNotif.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), NOTIFICATION_ID, intent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, reminderCal.getTimeInMillis(), pendingIntent);
    }


}