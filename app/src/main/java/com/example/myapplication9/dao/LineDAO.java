package com.example.myapplication9.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication9.database.LineTable;
import com.example.myapplication9.database.TransportDatabaseHelper;
import com.example.myapplication9.model.Line;

import java.util.ArrayList;
import java.util.List;

public class LineDAO {
    private SQLiteDatabase database;
    private TransportDatabaseHelper dbHelper;

    public LineDAO(Context context) {
        dbHelper = TransportDatabaseHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertLine(Line line) {
        ContentValues values = new ContentValues();
        values.put(LineTable.COLUMN_NAME, line.getName());
        values.put(LineTable.COLUMN_TYPE, line.getType());
        values.put(LineTable.COLUMN_COLOR, line.getColor());

        return database.insert(LineTable.TABLE_NAME, null, values);
    }

    public List<Line> getAllLines() {
        List<Line> lines = new ArrayList<>();

        Cursor cursor = database.query(
                LineTable.TABLE_NAME,
                null, null, null, null, null,
                LineTable.COLUMN_NAME + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Line line = cursorToLine(cursor);
                lines.add(line);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return lines;
    }

    public Line getLineById(long id) {
        Line line = null;
        Cursor cursor = database.query(
                LineTable.TABLE_NAME,
                null,
                LineTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            line = cursorToLine(cursor);
            cursor.close();
        }

        return line;
    }

    public boolean updateLine(Line line) {
        ContentValues values = new ContentValues();
        values.put(LineTable.COLUMN_NAME, line.getName());
        values.put(LineTable.COLUMN_TYPE, line.getType());
        values.put(LineTable.COLUMN_COLOR, line.getColor());

        return database.update(LineTable.TABLE_NAME, values,
                LineTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(line.getId())}) > 0;
    }

    public boolean deleteLine(long id) {
        return database.delete(LineTable.TABLE_NAME,
                LineTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    private Line cursorToLine(Cursor cursor) {
        Line line = new Line();
        line.setId(cursor.getLong(cursor.getColumnIndexOrThrow(LineTable.COLUMN_ID)));
        line.setName(cursor.getString(cursor.getColumnIndexOrThrow(LineTable.COLUMN_NAME)));
        line.setType(cursor.getString(cursor.getColumnIndexOrThrow(LineTable.COLUMN_TYPE)));
        line.setColor(cursor.getString(cursor.getColumnIndexOrThrow(LineTable.COLUMN_COLOR)));
        return line;
    }
}
