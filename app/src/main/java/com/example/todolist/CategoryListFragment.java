package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.todolist.DAO.CategoryDAO;
import com.example.todolist.adapter.CategoryListAdapter;
import com.example.todolist.common.AppDatabase;
import com.example.todolist.common.Constant;
import com.example.todolist.entity.Category;
import com.example.todolist.service.RecyclerViewClickListener;

import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment implements RecyclerViewClickListener {

    private List<Category> categoryList;
    private Button btnAddCategory;
    private RecyclerView rvCategoryList;
    private int categoryId;
    public RecyclerViewClickListener listener;
    private MainActivity mainActivity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CategoryListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryListFragment newInstance(String param1, String param2) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(context instanceof MainActivity) {
//            mainActivity = (MainActivity) context;
//        }
//    }

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
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rvCategoryList = view.findViewById(R.id.rv_category_list);
        rvCategoryList.setNestedScrollingEnabled(false);
        Context context = view.getContext();
        super.onViewCreated(view, savedInstanceState);
        AppDatabase db = Room
                .databaseBuilder(context, AppDatabase.class, "todoDB")
                .allowMainThreadQueries()
                .build();
        CategoryDAO categoryDAO = db.categoryDAO();

        btnAddCategory = view.findViewById(R.id.btn_add_category);
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText nameBox = new EditText(context);
                nameBox.setHint(Constant.CATEGORY_NAME_HINT);
                layout.addView(nameBox);
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(Constant.NEW_CATEGORY)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String categoryName = nameBox.getText().toString();
                                //check category name exist
                                Category category = categoryDAO.findByName(categoryName);
                                if (category == null) {
                                    //create category
                                    categoryDAO.createCategory(nameBox.getText().toString());
                                } else {
                                    Toast.makeText(view.getContext(), "This name already exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setView(layout);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //get category list
        categoryList = categoryDAO.getAll();
        CategoryListAdapter categoryListAdapter =
                new CategoryListAdapter(categoryList, CategoryListFragment.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new CategoryDetailDialog(categoryId).show(getActivity().getSupportFragmentManager(), "HEHEHE");
                    }
                }, "CategoryListFragment");
        rvCategoryList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        //notify data change
        categoryListAdapter.notifyDataSetChanged();
        rvCategoryList.setAdapter(categoryListAdapter);
    }


    @Override
    public void onCategoryClick(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public void onTaskClick(int taskId) {

    }

//    public void showCategoryAlertDialog(Category category) {
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//        final EditText editText = new EditText(context);
//        editText.setText(category.getName());
//        layout.addView(editText);
//        if (getActivity() != null) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
//                    .setTitle(Constant.CATEGORY_DETAIL_TITLE)
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .setMessage("abc")
//                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getContext(), "Please check the number you entered", Toast.LENGTH_LONG).show();
//                        }
//                    })
//                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getContext(), "Please check the number you entered", Toast.LENGTH_LONG).show();
//                        }
//                    });
////                .setView(layout);
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        }
//    }
}