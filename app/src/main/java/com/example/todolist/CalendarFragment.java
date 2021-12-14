package com.example.todolist;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.adapter.TaskListAdapter;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.entity.Task;
import com.example.todolist.model.DateItem;
import com.example.todolist.model.GeneralItem;
import com.example.todolist.model.ListItem;
import com.example.todolist.service.RecyclerViewClickListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements RecyclerViewClickListener {

    private CalendarView calendarView;
    private TaskListAdapter taskListAdapter;
    private RecyclerView rvTaskList;
    private List<Task> taskList;
    private TextView tvCalendarDate;
    private TextView tvNoTask;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    private TextView myDate;

    public String getTodayDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        return day + "/" + (month + 1) + "/" + year;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Context context = this.getContext();
        super.onViewCreated(view, savedInstanceState);
        calendarView = view.findViewById(R.id.cv_calendar);
        rvTaskList = view.findViewById(R.id.rv_tasks);
        tvCalendarDate = view.findViewById(R.id.tv_calendar_date);
        tvNoTask = view.findViewById(R.id.tv_notask);

        tvNoTask.setVisibility(View.GONE);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String selectedDate = sdf.format(new Date(calendarView.getDate()));
        tvCalendarDate.setText(selectedDate);

        AppDatabase db = Room.databaseBuilder(view.getContext(), AppDatabase.class, Constant.DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        TaskDAO taskDAO = db.taskDAO();
        taskList = taskDAO.getTasksByDate(getTodayDate());
        Map<Date, List<Task>> groupedHashMap = null;
        try {
            groupedHashMap = groupDataIntoHashMap(taskList);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<ListItem> consolidatedList = new ArrayList<>();

        for (Date dateTask : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(dateTask);
            consolidatedList.add(dateItem);
            for (Task task : groupedHashMap.get(dateTask)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setTask(task);
                consolidatedList.add(generalItem);
            }
        }
        taskListAdapter = new TaskListAdapter(context, consolidatedList,
                CalendarFragment.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get task detail by task id
            }
        }, "CalendarFragment");
        rvTaskList.setLayoutManager(new LinearLayoutManager(context));
        rvTaskList.setAdapter(taskListAdapter);
        //for default initial select date
        if (consolidatedList.isEmpty()) {
            tvNoTask.setVisibility(View.VISIBLE);
        }
        //change date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                tvCalendarDate.setText(selectedDate);
                AppDatabase db = Room.databaseBuilder(view.getContext(), AppDatabase.class, Constant.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .fallbackToDestructiveMigration()
                        .build();
                TaskDAO taskDAO = db.taskDAO();
                taskList = taskDAO.getTasksByDate(selectedDate);
                Map<Date, List<Task>> groupedHashMap = null;
                try {
                    groupedHashMap = groupDataIntoHashMap(taskList);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                List<ListItem> consolidatedList = new ArrayList<>();

                for (Date dateTask : groupedHashMap.keySet()) {
                    DateItem dateItem = new DateItem();
                    dateItem.setDate(dateTask);
                    consolidatedList.add(dateItem);
                    for (Task task : groupedHashMap.get(dateTask)) {
                        GeneralItem generalItem = new GeneralItem();
                        generalItem.setTask(task);
                        consolidatedList.add(generalItem);
                    }
                }

                if (consolidatedList.isEmpty()) {
                    tvNoTask.setVisibility(View.VISIBLE);
                } else {
                    tvNoTask.setVisibility(View.GONE);
                }
                //change date
                taskListAdapter = new TaskListAdapter(context, consolidatedList,
                        CalendarFragment.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get task detail by task id
                    }
                }, "CalendarFragment");
                rvTaskList.setLayoutManager(new LinearLayoutManager(context));
                rvTaskList.setAdapter(taskListAdapter);
            }
        });


    }

    @Override
    public void onCategoryClick(int categoryId) {

    }

    @Override
    public void onTaskClick(int taskId) {
        //get task id
    }

    private Map<Date, List<Task>> groupDataIntoHashMap(List<Task> taskList) throws ParseException {
        Collections.sort(taskList, (t1, t2) -> convertStringToLong(t1.getDate()) > convertStringToLong(t2.getDate()) ? 1 : -1);
        HashMap<Date, List<Task>> groupedHashMap = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Task task : taskList) {

            Date hashMapKey = new java.sql.Date(dateFormat.parse(task.getDate()).getTime());

            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(task);
            } else {
                List<Task> list = new ArrayList<>();
                list.add(task);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        Map<Date, List<Task>> m1 = new TreeMap<>(groupedHashMap);
//        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//
//        for (Map.Entry<Date, List<Task>> entry : m1.entrySet()) {
//            Log.i("TaskListFragment", "datee" + df.format(entry.getKey()));
//        }
        return m1;
    }

    public long convertStringToLong(String dateString) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        long milliseconds = 0;
        try {
            Date d = f.parse(dateString);
            milliseconds = d.getTime();
        } catch (Exception e) {
//            throw e;
            e.printStackTrace();
        }
        return milliseconds;
    }
}