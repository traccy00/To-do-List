package com.example.todolist.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.todolist.entity.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDAO {
    @Query("select * from tasks")
    List<Task> getAllTasks();

    @Query("INSERT INTO tasks(title, date, start_time, end_time, description, ring) " +
            "VALUES (:title, :date, :startTime, :endTime, :description, :ring)")
    void createTask(String title, String date, String startTime, String endTime, String description, String ring);

//    @Query("INSERT INTO tasks(title, date) VALUES (:title, :date)")
//    void createTask(String title, String date);
}
