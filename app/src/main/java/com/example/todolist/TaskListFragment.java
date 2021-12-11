package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.CategoryDAO;
import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.adapter.CategoryListAdapter;
import com.example.todolist.adapter.TaskListAdapter;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.entity.Category;
import com.example.todolist.entity.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment implements RecyclerViewClickListener {

    private RecyclerView rvTaskList, rvCategoryList;
    private List<Task> taskList;
    private List<Category> categoryList;
    private Button btnListCategory;
    private int categoryId;
    private int taskId;
    private TaskListAdapter taskListAdapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TaskListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskListFragment newInstance(String param1, String param2) {
        TaskListFragment fragment = new TaskListFragment();
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
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        rvTaskList = view.findViewById(R.id.rv_tasks);
        rvTaskList.setHasFixedSize(true);

        AppDatabase db = Room
                .databaseBuilder(context, AppDatabase.class, "todoDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        TaskDAO taskDAO = db.taskDAO();
        taskList = taskDAO.getAll();

        HashMap<String, List<Task>> groupedHashMap = groupDataIntoHashMap(taskList);
        List<ListItem> consolidatedList = new ArrayList<>();

        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);
            for (Task task : groupedHashMap.get(date)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setTask(task);
                consolidatedList.add(generalItem);
            }
        }
        taskListAdapter = new TaskListAdapter(this.getContext(), consolidatedList,
                TaskListFragment.this,"TaskListFragment");
        //category list
        rvCategoryList = view.findViewById(R.id.rv_categories);
        CategoryDAO categoryDAO = db.categoryDAO();
        categoryList = categoryDAO.getAll();
        categoryList.add(new Category(0, "All"));
        Collections.sort(categoryList, (o1, o2) -> o1.getId() < o2.getId() ? -1 : 1);
        //get category id
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryList,
                TaskListFragment.this, "TaskListFragment");
        //task list
        rvTaskList.setLayoutManager(new LinearLayoutManager(context));
        rvTaskList.setAdapter(taskListAdapter);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvCategoryList.setAdapter(categoryListAdapter);

        btnListCategory = view.findViewById(R.id.btn_list_category);
        btnListCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, new CategoryListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    boolean doubleBackToExit = false;

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//
//        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                // And when you want to go back based on your condition
//                if(doubleBackToExit) {
//                    this.setEnabled(false);
//                    requireActivity().onBackPressed();
//                }
//                doubleBackToExit = true;
//                Toast.makeText(context, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        doubleBackToExit = false;
//                    }
//                }, 2000);
//            }
//        };
//
//        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
//    }

    private HashMap<String, List<Task>> groupDataIntoHashMap(List<Task> taskList) {

        HashMap<String, List<Task>> groupedHashMap = new HashMap<>();

        for (Task task : taskList) {

            String hashMapKey = task.getDate();

            if (groupedHashMap.containsKey(hashMapKey)) {
                groupedHashMap.get(hashMapKey).add(task);
            } else {
                List<Task> list = new ArrayList<>();
                list.add(task);
                groupedHashMap.put(hashMapKey, list);
            }
        }
        return groupedHashMap;
    }

    @Override
    public void onCategoryClick(int categoryId) {
        Context context = this.getContext();
        this.categoryId = categoryId;
        AppDatabase db = Room
                .databaseBuilder(context, AppDatabase.class, "todoDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        TaskDAO taskDAO = db.taskDAO();
        if (categoryId != 0) {
            taskList = taskDAO.getTasksByCategoryId(categoryId);
        } else {
            taskList = taskDAO.getAll();
        }
        HashMap<String, List<Task>> groupedHashMap = groupDataIntoHashMap(taskList);
        List<ListItem> consolidatedList = new ArrayList<>();
        for (String date : groupedHashMap.keySet()) {
            DateItem dateItem = new DateItem();
            dateItem.setDate(date);
            consolidatedList.add(dateItem);
            for (Task task : groupedHashMap.get(date)) {
                GeneralItem generalItem = new GeneralItem();
                generalItem.setTask(task);
                consolidatedList.add(generalItem);
            }
        }
        taskListAdapter = new TaskListAdapter(this.getContext(), consolidatedList,
                TaskListFragment.this, "TaskListFragment");
        rvTaskList.setLayoutManager(new LinearLayoutManager(context));
        rvTaskList.setAdapter(taskListAdapter);
    }

    @Override
    public void onTaskClick(int taskId) {
        this.taskId = taskId;
    }
}