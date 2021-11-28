package com.example.todolist.common;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todolist.DAO.TaskDAO;
import com.example.todolist.entity.Task;

@Database(entities = {Task.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}
