package com.example.myapplication9.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication9.database.LineTable;
import com.example.myapplication9.database.ScheduleTable;
import com.example.myapplication9.database.StopTable;
import com.example.myapplication9.database.TransportDatabaseHelper;
import com.example.myapplication9.model.Schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleDAO {
    private SQLiteDatabase database;
    private TransportDatabaseHelper dbHelper;

    public ScheduleDAO(Context context) {
        dbHelper = TransportDatabaseHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(ScheduleTable.COLUMN_LINE_ID, schedule.getLineId());
        values.put(ScheduleTable.COLUMN_STOP_ID, schedule.getStopId());
        values.put(ScheduleTable.COLUMN_DEPARTURE_TIME, schedule.getDepartureTime());
        values.put(ScheduleTable.COLUMN_IS_WEEKDAY, schedule.isWeekday() ? 1 : 0);
        values.put(ScheduleTable.COLUMN_IS_WEEKEND, schedule.isWeekend() ? 1 : 0);

        return database.insert(ScheduleTable.TABLE_NAME, null, values);
    }

    public List<Schedule> getAllSchedules() {
        List<Schedule> schedules = new ArrayList<>();

        String query = "SELECT s.*, l." + LineTable.COLUMN_NAME + " AS line_name, " +
                "st." + StopTable.COLUMN_NAME + " AS stop_name " +
                "FROM " + ScheduleTable.TABLE_NAME + " s " +
                "JOIN " + LineTable.TABLE_NAME + " l ON s." + ScheduleTable.COLUMN_LINE_ID + " = l." + LineTable.COLUMN_ID + " " +
                "JOIN " + StopTable.TABLE_NAME + " st ON s." + ScheduleTable.COLUMN_STOP_ID + " = st." + StopTable.COLUMN_ID + " " +
                "ORDER BY s." + ScheduleTable.COLUMN_DEPARTURE_TIME;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Schedule schedule = cursorToSchedule(cursor);
                schedule.setLineName(cursor.getString(cursor.getColumnIndexOrThrow("line_name")));
                schedule.setStopName(cursor.getString(cursor.getColumnIndexOrThrow("stop_name")));
                schedules.add(schedule);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return schedules;
    }

    public List<Schedule> getSchedulesByLineAndStop(long lineId, long stopId) {
        List<Schedule> schedules = new ArrayList<>();

        String query = "SELECT s.*, l." + LineTable.COLUMN_NAME + " AS line_name, " +
                "st." + StopTable.COLUMN_NAME + " AS stop_name " +
                "FROM " + ScheduleTable.TABLE_NAME + " s " +
                "JOIN " + LineTable.TABLE_NAME + " l ON s." + ScheduleTable.COLUMN_LINE_ID + " = l." + LineTable.COLUMN_ID + " " +
                "JOIN " + StopTable.TABLE_NAME + " st ON s." + ScheduleTable.COLUMN_STOP_ID + " = st." + StopTable.COLUMN_ID + " " +
                "WHERE s." + ScheduleTable.COLUMN_LINE_ID + " = ? AND s." + ScheduleTable.COLUMN_STOP_ID + " = ? " +
                "ORDER BY s." + ScheduleTable.COLUMN_DEPARTURE_TIME;

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(lineId), String.valueOf(stopId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Schedule schedule = cursorToSchedule(cursor);
                schedule.setLineName(cursor.getString(cursor.getColumnIndexOrThrow("line_name")));
                schedule.setStopName(cursor.getString(cursor.getColumnIndexOrThrow("stop_name")));
                schedules.add(schedule);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return schedules;
    }

    public Schedule getScheduleById(long id) {
        Schedule schedule = null;

        String query = "SELECT s.*, l." + LineTable.COLUMN_NAME + " AS line_name, " +
                "st." + StopTable.COLUMN_NAME + " AS stop_name " +
                "FROM " + ScheduleTable.TABLE_NAME + " s " +
                "JOIN " + LineTable.TABLE_NAME + " l ON s." + ScheduleTable.COLUMN_LINE_ID + " = l." + LineTable.COLUMN_ID + " " +
                "JOIN " + StopTable.TABLE_NAME + " st ON s." + ScheduleTable.COLUMN_STOP_ID + " = st." + StopTable.COLUMN_ID + " " +
                "WHERE s." + ScheduleTable.COLUMN_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            schedule = cursorToSchedule(cursor);
            schedule.setLineName(cursor.getString(cursor.getColumnIndexOrThrow("line_name")));
            schedule.setStopName(cursor.getString(cursor.getColumnIndexOrThrow("stop_name")));
            cursor.close();
        }

        return schedule;
    }

    public boolean updateSchedule(Schedule schedule) {
        ContentValues values = new ContentValues();
        values.put(ScheduleTable.COLUMN_LINE_ID, schedule.getLineId());
        values.put(ScheduleTable.COLUMN_STOP_ID, schedule.getStopId());
        values.put(ScheduleTable.COLUMN_DEPARTURE_TIME, schedule.getDepartureTime());
        values.put(ScheduleTable.COLUMN_IS_WEEKDAY, schedule.isWeekday() ? 1 : 0);
        values.put(ScheduleTable.COLUMN_IS_WEEKEND, schedule.isWeekend() ? 1 : 0);

        return database.update(ScheduleTable.TABLE_NAME, values,
                ScheduleTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(schedule.getId())}) > 0;
    }

    public boolean deleteSchedule(long id) {
        return database.delete(ScheduleTable.TABLE_NAME,
                ScheduleTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    private Schedule cursorToSchedule(Cursor cursor) {
        Schedule schedule = new Schedule();
        schedule.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ScheduleTable.COLUMN_ID)));
        schedule.setLineId(cursor.getLong(cursor.getColumnIndexOrThrow(ScheduleTable.COLUMN_LINE_ID)));
        schedule.setStopId(cursor.getLong(cursor.getColumnIndexOrThrow(ScheduleTable.COLUMN_STOP_ID)));
        schedule.setDepartureTime(cursor.getString(cursor.getColumnIndexOrThrow(ScheduleTable.COLUMN_DEPARTURE_TIME)));
        schedule.setWeekday(cursor.getInt(cursor.getColumnIndexOrThrow(ScheduleTable.COLUMN_IS_WEEKDAY)) == 1);
        schedule.setWeekend(cursor.getInt(cursor.getColumnIndexOrThrow(ScheduleTable.COLUMN_IS_WEEKEND)) == 1);
        return schedule;
    }
}
