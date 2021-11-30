package com.example.todolist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.R;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.entity.Task;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.MyViewHolder> {

    private List<Task> taskList;

    public TaskListAdapter(List<Task> list) {
        this.taskList = list;
    }

    @NonNull
    @Override
    public TaskListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_task, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvTime, tvRing, tvView;
        CheckBox cbDone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvRing = itemView.findViewById(R.id.tv_ring);
            tvView = itemView.findViewById(R.id.tv_view);
            cbDone = itemView.findViewById(R.id.cb_done);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    int index = getAdapterPosition();
//                    Task task = taskList.get(index);
//                    Intent intent =new Intent(itemView.getContext(), )
                }
            });

            tvView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            cbDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index =getAdapterPosition();
                    Context context = itemView.getContext();
                    AppDatabase db = Room
                            .databaseBuilder(context, AppDatabase.class, "todoDB")
                            .allowMainThreadQueries()
                            .build();
                    TaskDAO taskDAO = db.taskDAO();
                    if(cbDone.isChecked()) {
                        taskDAO.updateTaskById(1, taskList.get(index).getId());
                    } else {
                        taskDAO.updateTaskById(0, taskList.get(index).getId());
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvTitle.setText(task.getTitle());
        holder.tvTime.setText(task.getStartTime() + "-" + task.getEndTime());
        if (task.getRing().matches("")) {
            holder.tvRing.setVisibility(View.GONE);
        }
        if (task.getIsDone() == 0) {
            holder.cbDone.setChecked(false);
        } else {
            holder.cbDone.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}
