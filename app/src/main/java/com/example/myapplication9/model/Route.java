package com.example.myapplication9.model;

public class Route {
    private long id;
    private long lineId;
    private long stopId;
    private int stopSequence;
    private double distanceFromStart;

    private String lineName;
    private String stopName;

    public Route() {
    }

    public Route(long lineId, long stopId, int stopSequence, double distanceFromStart) {
        this.lineId = lineId;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.distanceFromStart = distanceFromStart;
    }

    public Route(long id, long lineId, long stopId, int stopSequence, double distanceFromStart) {
        this.id = id;
        this.lineId = lineId;
        this.stopId = stopId;
        this.stopSequence = stopSequence;
        this.distanceFromStart = distanceFromStart;
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

    public int getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(int stopSequence) {
        this.stopSequence = stopSequence;
    }

    public double getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(double distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
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
        return "Line: " + lineName + ", Stop: " + stopName + ", Sequence: " + stopSequence;
    }
}
