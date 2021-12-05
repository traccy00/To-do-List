package com.example.todolist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.DateItem;
import com.example.todolist.GeneralItem;
import com.example.todolist.ListItem;
import com.example.todolist.R;
import com.example.todolist.RecyclerViewClickListener;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Task> taskList;
    private RecyclerViewClickListener recyclerViewClickListener;
    private View.OnClickListener itemClickListener;
    private String className;
    List<ListItem> consolidatedList = new ArrayList<>();
    private Context mContext;

    public TaskListAdapter(Context context,
                           List<ListItem> consolidatedList,
                           RecyclerViewClickListener recyclerViewClickListener,
                           View.OnClickListener itemClickListener,
                           String className) {
        this.mContext = context;
        this.consolidatedList = consolidatedList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.itemClickListener = itemClickListener;
        this.className = className;
    }

    public TaskListAdapter(Context context,
                           List<ListItem> consolidatedList,
                           RecyclerViewClickListener recyclerViewClickListener,
                           String className) {
        this.mContext = context;
        this.consolidatedList = consolidatedList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.className = className;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ListItem.TYPE_GENERAL:
                View v1 = inflater.inflate(R.layout.row_task, parent, false);
                viewHolder = new GeneralViewHolder(v1);
                break;
            case ListItem.TYPE_DATE:
                View v2 = inflater.inflate(R.layout.item_date, parent, false);
                viewHolder = new DateViewHolder(v2);
                break;
        }
        return viewHolder;
    }

//    class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tvTitle, tvTime, tvRing, tvView;
//        CheckBox cbDone;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            tvTitle = itemView.findViewById(R.id.tv_title);
//            tvTime = itemView.findViewById(R.id.tv_time);
//            tvRing = itemView.findViewById(R.id.tv_ring);
//            tvView = itemView.findViewById(R.id.tv_view);
//            cbDone = itemView.findViewById(R.id.cb_done);
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    int index = getAdapterPosition();
////                    Task task = taskList.get(index);
////                    Intent intent =new Intent(itemView.getContext(), )
//                }
//            });
//
//            tvView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            cbDone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index = getAdapterPosition();
//                    Context context = itemView.getContext();
//                    AppDatabase db = Room
//                            .databaseBuilder(context, AppDatabase.class, "todoDB")
//                            .allowMainThreadQueries()
//                            .build();
//                    TaskDAO taskDAO = db.taskDAO();
//                    if (cbDone.isChecked()) {
//                        taskDAO.updateTaskById(1, taskList.get(index).getId());
//                    } else {
//                        taskDAO.updateTaskById(0, taskList.get(index).getId());
//                    }
//                }
//            });
//        }
//    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case ListItem.TYPE_GENERAL:
                GeneralItem generalItem = (GeneralItem) consolidatedList.get(position);
                GeneralViewHolder generalViewHolder = (GeneralViewHolder) holder;
                generalViewHolder.tvTitle.setText(generalItem.getTask().getTitle());
                String startTime = generalItem.getTask().getStartTime();
                String endTime = generalItem.getTask().getEndTime();
                String time = startTime + "-" + endTime;
                if(!startTime.equals(Constant.START_TIME_DEFAULT) && !(endTime.equals(Constant.END_TIME_DEFAULT))) {
                    generalViewHolder.tvTime.setText(time);
                } else {
                    generalViewHolder.tvTime.setText(Constant.ALL_DAY);
                }
                if (generalItem.getTask().getRing().matches("")) {
                    generalViewHolder.tvRing.setVisibility(View.GONE);
                }
                if (generalItem.getTask().getIsDone() == 0) {
                    generalViewHolder.cbDone.setChecked(false);
                } else {
                    generalViewHolder.cbDone.setChecked(true);
                }
                if (className.equals("TaskListFragment")) {
                    //get task id onclick
                    recyclerViewClickListener.onTaskClick(generalItem.getTask().getId());
                    generalViewHolder.itemView.setOnClickListener(itemClickListener);
                }
                break;
            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.txtTitle.setText(dateItem.getDate());
                break;
        }
    }

    //ViewHolder for date row item
    class DateViewHolder extends RecyclerView.ViewHolder {

        protected TextView txtTitle;

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txtTitle = (TextView) itemView.findViewById(R.id.txt);
        }
    }

    //ViewHolder for general row item
    class GeneralViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvTime, tvRing, tvView;
        CheckBox cbDone;

        public GeneralViewHolder(@NonNull View itemView) {
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
                    int index = getAdapterPosition();
                    Context context = itemView.getContext();
                    AppDatabase db = Room
                            .databaseBuilder(context, AppDatabase.class, "todoDB")
                            .allowMainThreadQueries()
                            .build();
                    TaskDAO taskDAO = db.taskDAO();
                    GeneralItem generalItem = (GeneralItem) consolidatedList.get(index);
                    if (cbDone.isChecked()) {
                        taskDAO.updateTaskById(1, generalItem.getTask().getId());
                    } else {
                        taskDAO.updateTaskById(0, generalItem.getTask().getId());
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return consolidatedList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return consolidatedList != null ? consolidatedList.size() : 0;
    }

}
