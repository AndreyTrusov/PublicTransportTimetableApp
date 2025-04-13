package com.example.myapplication9.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication9.database.StopTable;
import com.example.myapplication9.database.TransportDatabaseHelper;
import com.example.myapplication9.model.Stop;

import java.util.ArrayList;
import java.util.List;

public class StopDAO {
    private SQLiteDatabase database;
    private TransportDatabaseHelper dbHelper;

    public StopDAO(Context context) {
        dbHelper = TransportDatabaseHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertStop(Stop stop) {
        ContentValues values = new ContentValues();
        values.put(StopTable.COLUMN_NAME, stop.getName());
        values.put(StopTable.COLUMN_LATITUDE, stop.getLatitude());
        values.put(StopTable.COLUMN_LONGITUDE, stop.getLongitude());
        values.put(StopTable.COLUMN_ACCESSIBILITY, stop.isAccessibility() ? 1 : 0);

        return database.insert(StopTable.TABLE_NAME, null, values);
    }

    public List<Stop> getAllStops() {
        List<Stop> stops = new ArrayList<>();

        Cursor cursor = database.query(
                StopTable.TABLE_NAME,
                null, null, null, null, null,
                StopTable.COLUMN_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Stop stop = cursorToStop(cursor);
                stops.add(stop);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return stops;
    }

    public Stop getStopById(long id) {
        Stop stop = null;
        Cursor cursor = database.query(
                StopTable.TABLE_NAME,
                null,
                StopTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            stop = cursorToStop(cursor);
            cursor.close();
        }

        return stop;
    }

    public boolean updateStop(Stop stop) {
        ContentValues values = new ContentValues();
        values.put(StopTable.COLUMN_NAME, stop.getName());
        values.put(StopTable.COLUMN_LATITUDE, stop.getLatitude());
        values.put(StopTable.COLUMN_LONGITUDE, stop.getLongitude());
        values.put(StopTable.COLUMN_ACCESSIBILITY, stop.isAccessibility() ? 1 : 0);

        return database.update(StopTable.TABLE_NAME, values,
                StopTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(stop.getId())}) > 0;
    }

    public boolean deleteStop(long id) {
        return database.delete(StopTable.TABLE_NAME,
                StopTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    private Stop cursorToStop(Cursor cursor) {
        Stop stop = new Stop();
        stop.setId(cursor.getLong(cursor.getColumnIndexOrThrow(StopTable.COLUMN_ID)));
        stop.setName(cursor.getString(cursor.getColumnIndexOrThrow(StopTable.COLUMN_NAME)));
        stop.setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow(StopTable.COLUMN_LATITUDE)));
        stop.setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow(StopTable.COLUMN_LONGITUDE)));
        stop.setAccessibility(cursor.getInt(cursor.getColumnIndexOrThrow(StopTable.COLUMN_ACCESSIBILITY)) == 1);
        return stop;
    }
}
