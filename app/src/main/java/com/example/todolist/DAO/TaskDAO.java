package com.example.todolist.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.todolist.entity.Task;

import java.util.Date;
import java.util.List;

@Dao
public interface TaskDAO {
    @Query("SELECT * from tasks order by date asc, start_time asc")
    List<Task> getAll();

    @Query("INSERT INTO tasks(title, date, start_time, end_time, description, ring, is_done) " +
            "VALUES (:title, :date, :startTime, :endTime, :description, :ring, :isDone)")
    void createTask(String title, String date, String startTime, String endTime, String description, String ring, int isDone);

    @Query("UPDATE tasks SET is_done = :isDone where id = :id")
    void updateTaskById(int isDone, int id);
}
