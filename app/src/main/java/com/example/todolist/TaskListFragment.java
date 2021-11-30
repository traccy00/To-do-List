package com.example.todolist;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.todolist.DAO.CategoryDAO;
import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.adapter.CategoryListAdapter;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.adapter.TaskListAdapter;
import com.example.todolist.entity.Category;
import com.example.todolist.entity.Task;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskListFragment extends Fragment {

    private RecyclerView rvTaskList, rvCategoryList;
    private List<Task> taskList;
    private List<Category> categoryList;
    private Button btnListCategory;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = view.getContext();
        rvTaskList = view.findViewById(R.id.rv_tasks);
        AppDatabase db = Room
                .databaseBuilder(context, AppDatabase.class, "todoDB")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        TaskDAO taskDAO = db.taskDAO();
        taskList = taskDAO.getAll();
        TaskListAdapter taskListAdapter = new TaskListAdapter(taskList);
        rvTaskList.setLayoutManager(new LinearLayoutManager(context));
        rvTaskList.setAdapter(taskListAdapter);

        rvCategoryList = view.findViewById(R.id.rv_categories);
        CategoryDAO categoryDAO = db.categoryDAO();
        categoryList = categoryDAO.getAll();
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(categoryList);
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
}