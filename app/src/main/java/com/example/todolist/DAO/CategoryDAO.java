package com.example.todolist.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.example.todolist.entity.Category;

import java.util.List;

@Dao
public interface CategoryDAO {
    @Query("select * from categories")
    List<Category> getAll();

    @Query("INSERT INTO categories(name) VALUES (:name)")
    void createCategory(String name);

    @Query("SELECT * from categories where name like :name")
    Category findByName(String name);

    @Query("SELECT * from categories where id = :categoryId")
    Category findById(int categoryId);

    @Query("UPDATE categories SET name = :editedName WHERE id = :categoryId")
    void updateCategory(String editedName, int categoryId);
}
