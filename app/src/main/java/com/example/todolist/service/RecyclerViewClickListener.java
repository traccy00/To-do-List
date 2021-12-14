package com.example.todolist.service;

import android.view.View;

import java.text.ParseException;

public interface RecyclerViewClickListener {
    void onCategoryClick(int categoryId) throws ParseException;
    void onTaskClick(int taskId);
}
