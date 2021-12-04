package com.example.todolist;

import android.view.View;

public interface RecyclerViewClickListener {
    void onCategoryClick(int categoryId);
    void onTaskClick(int taskId);
}
