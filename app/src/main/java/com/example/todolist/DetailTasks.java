package com.example.todolist;

import android.os.Bundle;
import android.widget.TextView;

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

        TextView tTitle = findViewById(R.id.taskTitleDet);
        TextView tDesc = findViewById(R.id.taskDescDet);
        TextView tDate = findViewById(R.id.taskDate);
        TextView tTime = findViewById(R.id.taskTime);


        String tId = getIntent().getStringExtra("id");

        da = new DataAccess(this);
        task = da.getTask(tId);

        tTitle.setText(task.getTitle());
        tDesc.setText(task.getDescription());
        tDate.setText(task.getDate());
        tTime.setText(task.getTime());




    }
}
