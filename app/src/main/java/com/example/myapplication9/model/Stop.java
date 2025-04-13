package com.example.myapplication9.model;

public class Stop {
    private long id;
    private String name;
    private double latitude;
    private double longitude;
    private boolean accessibility;

    public Stop() {
    }

    public Stop(String name, double latitude, double longitude, boolean accessibility) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accessibility = accessibility;
    }

    public Stop(long id, String name, double latitude, double longitude, boolean accessibility) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.accessibility = accessibility;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isAccessibility() {
        return accessibility;
    }

    public void setAccessibility(boolean accessibility) {
        this.accessibility = accessibility;
    }

    @Override
    public String toString() {
        return name;
    }
}
