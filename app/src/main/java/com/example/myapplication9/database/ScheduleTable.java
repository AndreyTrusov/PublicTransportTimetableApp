package com.example.myapplication9.database;

public class ScheduleTable {
    public static final String TABLE_NAME = "schedules";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINE_ID = "line_id";
    public static final String COLUMN_STOP_ID = "stop_id";
    public static final String COLUMN_DEPARTURE_TIME = "departure_time";
    public static final String COLUMN_IS_WEEKDAY = "is_weekday";
    public static final String COLUMN_IS_WEEKEND = "is_weekend";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LINE_ID + " INTEGER NOT NULL, " +
                    COLUMN_STOP_ID + " INTEGER NOT NULL, " +
                    COLUMN_DEPARTURE_TIME + " TEXT NOT NULL, " +
                    COLUMN_IS_WEEKDAY + " INTEGER DEFAULT 0, " +
                    COLUMN_IS_WEEKEND + " INTEGER DEFAULT 0, " +
                    "FOREIGN KEY (" + COLUMN_LINE_ID + ") REFERENCES " + LineTable.TABLE_NAME + "(" + LineTable.COLUMN_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY (" + COLUMN_STOP_ID + ") REFERENCES " + StopTable.TABLE_NAME + "(" + StopTable.COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";
}
