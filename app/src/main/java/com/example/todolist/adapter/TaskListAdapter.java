package com.example.todolist.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.CategoryDAO;
import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.DateItem;
import com.example.todolist.GeneralItem;
import com.example.todolist.ListItem;
import com.example.todolist.R;
import com.example.todolist.RecyclerViewClickListener;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.entity.Category;
import com.example.todolist.entity.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private RecyclerViewClickListener recyclerViewClickListener;
    private View.OnClickListener itemClickListener;
    private String className;
    List<ListItem> consolidatedList = new ArrayList<>();
    private Context mContext;
    private SpinnerAdapter adapter;

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
                if (!startTime.equals(Constant.START_TIME_DEFAULT) && !(endTime.equals(Constant.END_TIME_DEFAULT))) {
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
//                    generalViewHolder.itemView.setOnClickListener(itemClickListener);
                }
                break;
            case ListItem.TYPE_DATE:
                DateItem dateItem = (DateItem) consolidatedList.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                if(className.equals("CalendarFragment")) {
                    dateViewHolder.txtTitle.setVisibility(View.GONE);
                }
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                dateViewHolder.txtTitle.setText(df.format(dateItem.getDate()));
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
        int selectCategoryId;

        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvRing = itemView.findViewById(R.id.tv_ring);
            tvView = itemView.findViewById(R.id.tv_view);
            cbDone = itemView.findViewById(R.id.cb_done);

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    int index = getAdapterPosition();
                    GeneralItem generalItem = (GeneralItem) consolidatedList.get(index);
                    Task task = generalItem.getTask();
                    int taskId = task.getId();
                    AppDatabase db = Room.databaseBuilder(itemView.getContext(), AppDatabase.class, Constant.DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                    CategoryDAO categoryDAO = db.categoryDAO();
                    Category category = categoryDAO.findById(task.getCategoryId());
                    String categoryName;
                    if (category == null) {
                        categoryName = "Default";
                    } else {
                        categoryName = category.getName();
                    }

                    String time;
                    if (task.getStartTime().equals(Constant.START_TIME_DEFAULT) && task.getEndTime().equals(Constant.END_TIME_DEFAULT)) {
                        time = "All day";
                    } else {
                        time = task.getStartTime() + "-" + task.getEndTime();
                    }

                    final BottomSheetDialog bottomSheetDialog = new
                            BottomSheetDialog(itemView.getContext(), R.style.BottomSheetDialogTheme);
                    View bottomSheetView = LayoutInflater.from(itemView.getContext())
                            .inflate(R.layout.layout_sheet_dialog, (LinearLayout) itemView.findViewById(R.id.bottomSheetContainer));

                    TextView tvTitle = bottomSheetView.findViewById(R.id.tv_title_sheet);
                    TextView tvDatetime = bottomSheetView.findViewById(R.id.tv_datetime_sheet);
                    TextView tvRing = bottomSheetView.findViewById(R.id.tv_ring_sheet);
                    TextView tvDescription = bottomSheetView.findViewById(R.id.tv_description_sheet);
                    TextView tvCategory = bottomSheetView.findViewById(R.id.tv_category_sheet);
                    TableRow tableRow = bottomSheetView.findViewById(R.id.tablerow_datetime);

                    tvTitle.setText(task.getTitle());
                    tvDatetime.setText(task.getDate() + "\n" + time);
                    tvRing.setText(task.getRing());
                    tvDescription.setText(task.getDescription());
                    tvCategory.setText(categoryName);

                    Button btnEdit = bottomSheetView.findViewById(R.id.btn_edit);
                    Button btnDelete = bottomSheetView.findViewById(R.id.btn_delete);
                    Button btnClose = bottomSheetView.findViewById(R.id.btn_close);
                    Button btnSave = bottomSheetView.findViewById(R.id.btn_save);
                    Button btnMarkAsCompleted = bottomSheetView.findViewById(R.id.btn_mark_complete);
                    CheckBox allDay = bottomSheetView.findViewById(R.id.checkBox);
                    Button btnAddTime = bottomSheetView.findViewById(R.id.btn_add_time_sheet);

                    EditText edtTitle = bottomSheetView.findViewById(R.id.edt_title);
                    EditText edtDate = bottomSheetView.findViewById(R.id.edt_date_sheet);
                    EditText edtStartTime = bottomSheetView.findViewById(R.id.edt_start_time_sheet);
                    EditText edtEndTime = bottomSheetView.findViewById(R.id.edt_end_time_sheet);
                    EditText edtRing = bottomSheetView.findViewById(R.id.edt_ring);
                    EditText edtDescription = bottomSheetView.findViewById(R.id.edt_description);
                    Spinner spinner = bottomSheetView.findViewById(R.id.spinner_category_sheet);
                    //spinner category list
                    List<Category> categoryList = categoryDAO.getAll();
                    categoryList.add(new Category(0, "Default"));
                    adapter = new SpinnerAdapter(itemView.getContext(), android.R.layout.simple_spinner_item, categoryList.toArray(new Category[categoryList.size()]));
                    spinner.setAdapter(adapter);
                    String finalCategoryName = categoryName;
                    List<Category> categoryList1 = categoryList.stream().filter(c -> c.getName().equals(finalCategoryName)).collect(Collectors.toList());
                    int spinnerPosition = adapter.getPosition(categoryList1.get(0));
                    spinner.setSelection(spinnerPosition);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Category selectCategory = adapter.getItem(position);
                            selectCategoryId = selectCategory.getId();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    String finalTime = time;
                    btnEdit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //view detail gone
                            tvTitle.setVisibility(View.GONE);
                            tvDatetime.setVisibility(View.GONE);
                            tvRing.setVisibility(View.GONE);
                            tvDescription.setVisibility(View.GONE);
                            tvCategory.setVisibility(View.GONE);
                            btnMarkAsCompleted.setVisibility(View.GONE);
                            btnEdit.setVisibility(View.GONE);
                            //edit screen for detail visible
                            tableRow.setVisibility(View.VISIBLE);
                            allDay.setVisibility(View.VISIBLE);
                            edtTitle.setVisibility(View.VISIBLE);
                            edtDate.setVisibility(View.VISIBLE);
                            if (finalTime.equals("All day")) {
                                allDay.setChecked(true);
                                edtStartTime.setVisibility(View.GONE);
                                edtEndTime.setVisibility(View.GONE);
                                btnAddTime.setVisibility(View.VISIBLE);
                            } else {
                                edtStartTime.setText(task.getStartTime());
                                edtEndTime.setText(task.getEndTime());
                                edtStartTime.setVisibility(View.VISIBLE);
                                edtEndTime.setVisibility(View.VISIBLE);
                                btnAddTime.setVisibility(View.GONE);
                            }
                            edtRing.setVisibility(View.VISIBLE);
                            edtDescription.setVisibility(View.VISIBLE);
                            spinner.setVisibility(View.VISIBLE);

                            edtTitle.setText(task.getTitle());
                            edtDate.setText(task.getDate());

                            edtRing.setText(task.getRing());

                            allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if (allDay.isChecked()) {
                                        edtStartTime.setVisibility(View.GONE);
                                        edtEndTime.setVisibility(View.GONE);
                                        btnAddTime.setVisibility(View.VISIBLE);
                                        edtStartTime.setText(Constant.START_TIME_DEFAULT);
                                        edtEndTime.setText(Constant.END_TIME_DEFAULT);
                                    } else {
                                        edtStartTime.setVisibility(View.VISIBLE);
                                        edtEndTime.setVisibility(View.VISIBLE);
                                        btnAddTime.setVisibility(View.GONE);
                                        Calendar calendar = Calendar.getInstance();
                                        edtStartTime.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                                        edtEndTime.setText((calendar.get(Calendar.HOUR_OF_DAY) + 1) + ":" + calendar.get(Calendar.MINUTE));
                                    }
                                }
                            });

                            btnAddTime.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    allDay.setChecked(false);
                                    edtStartTime.setVisibility(View.VISIBLE);
                                    edtEndTime.setVisibility(View.VISIBLE);
                                    btnAddTime.setVisibility(View.GONE);
                                }
                            });
                            edtDescription.setText(task.getDate());

                            btnSave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    TaskDAO taskDAO = db.taskDAO();
                                    taskDAO.updateTaskDetail(taskId, edtTitle.getText().toString(),
                                            edtDate.getText().toString(), edtStartTime.getText().toString(),
                                            edtEndTime.getText().toString(), edtDescription.getText().toString(),
                                            edtRing.getText().toString(), selectCategoryId);
                                    Toast.makeText(itemView.getContext(), "Task updated", Toast.LENGTH_SHORT).show();
                                    bottomSheetDialog.dismiss();
                                }
                            });

                        }
                    });
                    btnMarkAsCompleted.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TaskDAO taskDAO = db.taskDAO();
                            taskDAO.updateTaskById(1, taskId);
                            Toast.makeText(itemView.getContext(), "Task updated", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                        }
                    });

                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TaskDAO taskDAO = db.taskDAO();
                            taskDAO.deleteById(taskId);
                            bottomSheetDialog.dismiss();
                        }
                    });

                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bottomSheetDialog.dismiss();
                        }
                    });

                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
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
