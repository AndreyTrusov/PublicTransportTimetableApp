package com.example.myapplication9.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransportDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "transport_timetable.db";
    private static final int DATABASE_VERSION = 1;

    private static TransportDatabaseHelper instance;

    public static synchronized TransportDatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TransportDatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private TransportDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LineTable.CREATE_TABLE);
        db.execSQL(StopTable.CREATE_TABLE);
        db.execSQL(RouteTable.CREATE_TABLE);
        db.execSQL(ScheduleTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ScheduleTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RouteTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StopTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LineTable.TABLE_NAME);
        onCreate(db);
    }
}
