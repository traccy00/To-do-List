package com.example.todolist.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.CategoryListFragment;
import com.example.todolist.R;
import com.example.todolist.RecyclerViewClickListener;
import com.example.todolist.entity.Category;

import java.text.ParseException;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {

    private int selectedPos = RecyclerView.NO_POSITION;
    int saveLastSelectedPos = -1;
    private List<Category> categoryList;
    private RecyclerViewClickListener recyclerViewClickListener;
    private String className;
    public View.OnClickListener itemClickListener;
    public RecyclerView rvTaskList;

    public CategoryListAdapter() {}

    public CategoryListAdapter(List<Category> categoryList, RecyclerViewClickListener recyclerViewClickListener, String className) {
        this.categoryList = categoryList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.className = className;
    }

    public CategoryListAdapter(List<Category> categoryList,RecyclerViewClickListener recyclerViewClickListener, View.OnClickListener itemClickListener, String className) {
        this.categoryList = categoryList;
        this.recyclerViewClickListener = recyclerViewClickListener;
        this.itemClickListener = itemClickListener;
        this.className = className;
    }

    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(v);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, int position) {
        if (saveLastSelectedPos == selectedPos) {// khi cùng một category click 2 lần nghĩa là deselect
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        } else {//nếu không sẽ xem xét position nào được chọn để hightlight background cho nó
            holder.itemView.setBackgroundColor(selectedPos == position ? Color.GRAY : Color.TRANSPARENT);
        }
        Category category = categoryList.get(position);
        holder.tvName.setText(category.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (className.equals("AddTaskActivity")) {
                    saveLastSelectedPos = selectedPos;//lúc này selectedPost vẫn giữ giá trị cũ, ta lấy giá trị này
                    selectedPos = holder.getLayoutPosition();//giá trị được click mới
                    if (saveLastSelectedPos == selectedPos) {//category deselect nghĩa là không được chọn
                        selectedPos = -1;//ko category nao dc chon
                        try {
                            recyclerViewClickListener.onCategoryClick(0);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            recyclerViewClickListener.onCategoryClick(category.getId());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    notifyDataSetChanged();
                } else if (className.equals("CategoryListFragment")) {
                    try {
                        recyclerViewClickListener.onCategoryClick(category.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    holder.itemView.setOnClickListener(itemClickListener);
                } else if(className.equals("TaskListFragment")) {
                    try {
                        recyclerViewClickListener.onCategoryClick(category.getId());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
