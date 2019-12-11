package com.example.todolist;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.DataBase.DataAccess;
import com.example.todolist.Tasks.Tasks;

public class DetailTasks extends AppCompatActivity {
    DataAccess da;
    Tasks task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tasks);

        //back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tTitle = findViewById(R.id.taskTitleDet);
        TextView tDesc = findViewById(R.id.taskDescDet);
        TextView tDate = findViewById(R.id.taskDate);
        TextView tTime = findViewById(R.id.taskTime);
        TextView tLocation = findViewById(R.id.taskLocation);

        String tId = getIntent().getStringExtra("id");

        da = new DataAccess(this);
        task = da.getTask(tId);

        tTitle.setText(task.getTitle());
        tDesc.setText(task.getDescription());
        tDate.setText(task.getDate());
        tTime.setText(task.getTime());
        tLocation.setText(task.getPlace());

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

}
