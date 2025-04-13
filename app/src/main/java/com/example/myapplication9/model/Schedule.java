package com.example.myapplication9.model;

public class Schedule {
    private long id;
    private long lineId;
    private long stopId;
    private String departureTime;
    private boolean isWeekday;
    private boolean isWeekend;

    private String lineName;
    private String stopName;

    public Schedule() {
    }

    public Schedule(long lineId, long stopId, String departureTime, boolean isWeekday, boolean isWeekend) {
        this.lineId = lineId;
        this.stopId = stopId;
        this.departureTime = departureTime;
        this.isWeekday = isWeekday;
        this.isWeekend = isWeekend;
    }

    public Schedule(long id, long lineId, long stopId, String departureTime, boolean isWeekday, boolean isWeekend) {
        this.id = id;
        this.lineId = lineId;
        this.stopId = stopId;
        this.departureTime = departureTime;
        this.isWeekday = isWeekday;
        this.isWeekend = isWeekend;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public long getStopId() {
        return stopId;
    }

    public void setStopId(long stopId) {
        this.stopId = stopId;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public boolean isWeekday() {
        return isWeekday;
    }

    public void setWeekday(boolean weekday) {
        isWeekday = weekday;
    }

    public boolean isWeekend() {
        return isWeekend;
    }

    public void setWeekend(boolean weekend) {
        isWeekend = weekend;
    }

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    @Override
    public String toString() {
        return "Line: " + lineName + ", Stop: " + stopName + ", Time: " + departureTime;
    }
}
