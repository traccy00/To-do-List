package com.example.todolist;

public class BarChartData {
    float id;
    float taskPercent;
    String date;

    public BarChartData() {
    }

    public BarChartData(float id, float taskPercent, String date) {
        this.id = id;
        this.taskPercent = taskPercent;
        this.date = date;
    }

    public float getId() {
        return id;
    }

    public void setId(float id) {
        this.id = id;
    }

    public float getTaskPercent() {
        return taskPercent;
    }

    public void setTaskPercent(float taskPercent) {
        this.taskPercent = taskPercent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
