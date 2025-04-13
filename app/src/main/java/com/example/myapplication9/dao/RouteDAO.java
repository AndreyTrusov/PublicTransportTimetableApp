package com.example.myapplication9.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication9.database.LineTable;
import com.example.myapplication9.database.RouteTable;
import com.example.myapplication9.database.StopTable;
import com.example.myapplication9.database.TransportDatabaseHelper;
import com.example.myapplication9.model.Route;

import java.util.ArrayList;
import java.util.List;

public class RouteDAO {
    private SQLiteDatabase database;
    private TransportDatabaseHelper dbHelper;

    public RouteDAO(Context context) {
        dbHelper = TransportDatabaseHelper.getInstance(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertRoute(Route route) {
        ContentValues values = new ContentValues();
        values.put(RouteTable.COLUMN_LINE_ID, route.getLineId());
        values.put(RouteTable.COLUMN_STOP_ID, route.getStopId());
        values.put(RouteTable.COLUMN_STOP_SEQUENCE, route.getStopSequence());
        values.put(RouteTable.COLUMN_DISTANCE_FROM_START, route.getDistanceFromStart());

        return database.insert(RouteTable.TABLE_NAME, null, values);
    }

    public List<Route> getAllRoutes() {
        List<Route> routes = new ArrayList<>();

        String query = "SELECT r.*, l." + LineTable.COLUMN_NAME + " AS line_name, " +
                "s." + StopTable.COLUMN_NAME + " AS stop_name " +
                "FROM " + RouteTable.TABLE_NAME + " r " +
                "JOIN " + LineTable.TABLE_NAME + " l ON r." + RouteTable.COLUMN_LINE_ID + " = l." + LineTable.COLUMN_ID + " " +
                "JOIN " + StopTable.TABLE_NAME + " s ON r." + RouteTable.COLUMN_STOP_ID + " = s." + StopTable.COLUMN_ID + " " +
                "ORDER BY r." + RouteTable.COLUMN_LINE_ID + ", r." + RouteTable.COLUMN_STOP_SEQUENCE;

        Cursor cursor = database.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Route route = cursorToRoute(cursor);
                route.setLineName(cursor.getString(cursor.getColumnIndexOrThrow("line_name")));
                route.setStopName(cursor.getString(cursor.getColumnIndexOrThrow("stop_name")));
                routes.add(route);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return routes;
    }

    public List<Route> getRoutesByLineId(long lineId) {
        List<Route> routes = new ArrayList<>();

        String query = "SELECT r.*, l." + LineTable.COLUMN_NAME + " AS line_name, " +
                "s." + StopTable.COLUMN_NAME + " AS stop_name " +
                "FROM " + RouteTable.TABLE_NAME + " r " +
                "JOIN " + LineTable.TABLE_NAME + " l ON r." + RouteTable.COLUMN_LINE_ID + " = l." + LineTable.COLUMN_ID + " " +
                "JOIN " + StopTable.TABLE_NAME + " s ON r." + RouteTable.COLUMN_STOP_ID + " = s." + StopTable.COLUMN_ID + " " +
                "WHERE r." + RouteTable.COLUMN_LINE_ID + " = ? " +
                "ORDER BY r." + RouteTable.COLUMN_STOP_SEQUENCE;

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(lineId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Route route = cursorToRoute(cursor);
                route.setLineName(cursor.getString(cursor.getColumnIndexOrThrow("line_name")));
                route.setStopName(cursor.getString(cursor.getColumnIndexOrThrow("stop_name")));
                routes.add(route);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return routes;
    }

    public Route getRouteById(long id) {
        Route route = null;

        String query = "SELECT r.*, l." + LineTable.COLUMN_NAME + " AS line_name, " +
                "s." + StopTable.COLUMN_NAME + " AS stop_name " +
                "FROM " + RouteTable.TABLE_NAME + " r " +
                "JOIN " + LineTable.TABLE_NAME + " l ON r." + RouteTable.COLUMN_LINE_ID + " = l." + LineTable.COLUMN_ID + " " +
                "JOIN " + StopTable.TABLE_NAME + " s ON r." + RouteTable.COLUMN_STOP_ID + " = s." + StopTable.COLUMN_ID + " " +
                "WHERE r." + RouteTable.COLUMN_ID + " = ?";

        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            route = cursorToRoute(cursor);
            route.setLineName(cursor.getString(cursor.getColumnIndexOrThrow("line_name")));
            route.setStopName(cursor.getString(cursor.getColumnIndexOrThrow("stop_name")));
            cursor.close();
        }

        return route;
    }

    public boolean updateRoute(Route route) {
        ContentValues values = new ContentValues();
        values.put(RouteTable.COLUMN_LINE_ID, route.getLineId());
        values.put(RouteTable.COLUMN_STOP_ID, route.getStopId());
        values.put(RouteTable.COLUMN_STOP_SEQUENCE, route.getStopSequence());
        values.put(RouteTable.COLUMN_DISTANCE_FROM_START, route.getDistanceFromStart());

        return database.update(RouteTable.TABLE_NAME, values,
                RouteTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(route.getId())}) > 0;
    }

    public boolean deleteRoute(long id) {
        return database.delete(RouteTable.TABLE_NAME,
                RouteTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    private Route cursorToRoute(Cursor cursor) {
        Route route = new Route();
        route.setId(cursor.getLong(cursor.getColumnIndexOrThrow(RouteTable.COLUMN_ID)));
        route.setLineId(cursor.getLong(cursor.getColumnIndexOrThrow(RouteTable.COLUMN_LINE_ID)));
        route.setStopId(cursor.getLong(cursor.getColumnIndexOrThrow(RouteTable.COLUMN_STOP_ID)));
        route.setStopSequence(cursor.getInt(cursor.getColumnIndexOrThrow(RouteTable.COLUMN_STOP_SEQUENCE)));
        route.setDistanceFromStart(cursor.getDouble(cursor.getColumnIndexOrThrow(RouteTable.COLUMN_DISTANCE_FROM_START)));
        return route;
    }
}
