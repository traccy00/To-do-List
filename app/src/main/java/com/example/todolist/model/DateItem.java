package com.example.todolist.model;

import java.util.Date;

public class DateItem extends ListItem {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
