package com.example.myapplication9.database;

public class RouteTable {
    public static final String TABLE_NAME = "routes";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LINE_ID = "line_id";
    public static final String COLUMN_STOP_ID = "stop_id";
    public static final String COLUMN_STOP_SEQUENCE = "stop_sequence";
    public static final String COLUMN_DISTANCE_FROM_START = "distance_from_start";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LINE_ID + " INTEGER NOT NULL, " +
                    COLUMN_STOP_ID + " INTEGER NOT NULL, " +
                    COLUMN_STOP_SEQUENCE + " INTEGER NOT NULL, " +
                    COLUMN_DISTANCE_FROM_START + " REAL, " +
                    "FOREIGN KEY (" + COLUMN_LINE_ID + ") REFERENCES " + LineTable.TABLE_NAME + "(" + LineTable.COLUMN_ID + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY (" + COLUMN_STOP_ID + ") REFERENCES " + StopTable.TABLE_NAME + "(" + StopTable.COLUMN_ID + ") ON DELETE CASCADE" +
                    ")";
}
