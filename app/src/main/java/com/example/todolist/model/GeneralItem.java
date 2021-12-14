package com.example.todolist.model;

import com.example.todolist.entity.Task;

public class GeneralItem extends ListItem {
    private Task task;

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
