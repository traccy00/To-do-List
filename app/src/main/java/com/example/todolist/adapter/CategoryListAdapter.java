package com.example.todolist.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddTaskActivity;
import com.example.todolist.R;
import com.example.todolist.RecyclerViewClickListener;
import com.example.todolist.entity.Category;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.MyViewHolder> {
    private List<Category> categoryList;
    private RecyclerViewClickListener recyclerViewClickListener;
    public CategoryListAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public CategoryListAdapter(List<Category> categoryList, RecyclerViewClickListener recyclerViewClickListener) {
        this.categoryList = categoryList;
        this.recyclerViewClickListener = recyclerViewClickListener;
    }

    @NonNull
    @Override
    public CategoryListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(v);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_category_name);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.MyViewHolder holder, int position) {
            Category category = categoryList.get(position);
            holder.tvName.setText(category.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerViewClickListener.onCategoryClick(category.getId());
                }
            });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
