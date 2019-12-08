package com.example.todolist.DataBase;

public final class DataBaseContract {

    public final static String DB_NAME= "DataBase1.db";

    public abstract static class TasksTable{

        public static final String TASKS_TABLE_NAME = "tasks";

        public static final String ID_FIELD = "id_task";
        public static final String TITLE_FIELD = "title";
        public static final String DESC_FIELD = "description";
        public static final String DATE_FIELD = "date";
        public static final String TIME_FIELD = "time";
        public static final String PLACE_FIELD = "place";

    }

}
