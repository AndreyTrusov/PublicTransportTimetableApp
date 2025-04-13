package com.example.myapplication9.database;

public class LineTable {
    public static final String TABLE_NAME = "lines";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_COLOR = "color";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_TYPE + " TEXT NOT NULL, " +
                    COLUMN_COLOR + " TEXT NOT NULL" +
                    ")";
}
