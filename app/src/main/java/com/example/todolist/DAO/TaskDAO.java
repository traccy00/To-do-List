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

    @Query("INSERT INTO tasks(title, date, start_time, end_time, description, ring, is_done, category_id) " +
            "VALUES (:title, :date, :startTime, :endTime, :description, :ring, :isDone, :categoryId)")
    void createTask(String title, String date, String startTime, String endTime, String description, String ring, int isDone, int categoryId);

    @Query("UPDATE tasks SET is_done = :isDone where id = :id")
    void updateTaskById(int isDone, int id);

    @Query("SELECT * FROM tasks where category_id = :categoryId")
    List<Task> getTasksByCategoryId(int categoryId);

    @Query("SELECT * FROM tasks where date = :date")
    List<Task> getTasksByDate(String date);

    @Query("SELECT count(*) FROM tasks where date like :date")
    int countAllTasksInDate(String date);

    @Query("SELECT count(*) FROM tasks where date like :date and is_done = :isDone")
    int countIsDoneTasksInDate(String date, int isDone);

    @Query("SELECT * from tasks where id = :id")
    Task getById(int id);

    @Query("UPDATE tasks SET title = :updateTitle, date = :updateDate, start_time = :updateStartTime, " +
            "end_time = :updateEndTime, description = :updateDescription, ring = :updateRing, " +
            "category_id = :updateCategoryId where id = :taskId")
    void updateTaskDetail(int taskId, String updateTitle, String updateDate, String updateStartTime,
                          String updateEndTime, String updateDescription, String updateRing,
                          int updateCategoryId);

    @Query("DELETE FROM tasks where id = :taskId")
    void deleteById(int taskId);


    @Query("SELECT count(*) FROM tasks where is_done = :isDone")
    int countIsDoneTasks(int isDone);

    @Query("SELECT count(*) FROM tasks")
    int countAllTasks();

}
