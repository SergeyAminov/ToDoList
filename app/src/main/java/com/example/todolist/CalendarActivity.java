package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import com.example.todolist.CreateEvent.CreateEventActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private String date_ev;
    public static final String KEY_DATE_EVENT = "date";
    public static final String IS_CHECKBOX_ACTIVE = "is checked";
    public static final String KEYWORD_CALENDAR = "fromCalendarActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        date_ev = format.format(calendarView.getDate());

        calendarView.setDate(new Date().getTime());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date_ev = dayOfMonth + "/" + (month+1) + "/" + (year);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        //Log.d(LOG_TAG, "onStart");
    }

    @Override
    public void onPause(){
        super.onPause();
        //Log.d(LOG_TAG, "onPause");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        //Log.d(LOG_TAG, "onRestart");
    }

    @Override
    public void onResume(){
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
        super.onDestroy();
        //Log.d(LOG_TAG, "onDestroy");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.MenuID) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (id == R.id.MapsID) {
            startActivity(new Intent(this, MapActivity.class));
        }
        if (id == R.id.SettingsID) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void createEvent(View view) {

        Intent i = new Intent(this, CreateEventActivity.class);
        i.putExtra(KEYWORD_CALENDAR,true);
        i.putExtra(KEY_DATE_EVENT, this.date_ev);
        i.putExtra(IS_CHECKBOX_ACTIVE, true);
        startActivity(i);

    }

}
