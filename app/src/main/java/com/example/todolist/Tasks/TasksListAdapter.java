package com.example.todolist.Tasks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.DetailTasks;
import com.example.todolist.R;

import java.util.ArrayList;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.TasksViewHolder>{
    private ArrayList<Tasks> tList;
    private Context mContext;


    public TasksListAdapter(Context context, ArrayList<Tasks> list) {
        this.mContext = context;
        this.tList = list;
    }

    @NonNull
    @Override
    public TasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.tasklist_item, parent, false);
        TasksViewHolder vh = new TasksViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull TasksViewHolder holder, int position) {
        Tasks currentTask = tList.get(position);
        holder.bindTo(currentTask);
    }

    @Override
    public int getItemCount() {
        return tList.size();
    }


    class TasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tTitle;
        private TextView tDescription;


        TasksViewHolder(View v) {
            super(v);

            tTitle = v.findViewById(R.id.taskTitle);
            tDescription = v.findViewById(R.id.taskDesc);
            v.setOnClickListener(this);
        }

        void bindTo(Tasks currentTask){
            // Populate the textviews with data.
            tTitle.setText(currentTask.getTitle());
            tDescription.setText(currentTask.getDescription());

        }
        @Override
        public void onClick(View view) {
            Tasks currentTask = tList.get(getAdapterPosition());
            Intent detailIntent = new Intent(mContext, DetailTasks.class);
            detailIntent.putExtra("id", currentTask.getId());
            mContext.startActivity(detailIntent);

        }
    }

}
