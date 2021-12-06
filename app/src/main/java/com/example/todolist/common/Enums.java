package com.example.todolist.common;

public class Enums {

    public enum TaskStatus {
        COMPLETED("Completed"), NOT_COMPLETED("Not completed");

        private String taskStatus;

        TaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

    }
}
