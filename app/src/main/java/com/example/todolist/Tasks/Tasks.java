package com.example.todolist.Tasks;

public class Tasks{
    private String title;
    private String description;
    private String id;
    private String date;
    private String time;
    private String place;

    public Tasks(String id, String title, String description, String date, String time, String place) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.date = date;
        this.time = time;
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public Tasks(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }




    public String getId(){ return id;}

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
