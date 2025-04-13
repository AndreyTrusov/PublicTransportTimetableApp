package com.example.myapplication9.model;

public class Line {
    private long id;
    private String name;
    private String type;
    private String color;

    public Line() {
    }

    public Line(String name, String type, String color) {
        this.name = name;
        this.type = type;
        this.color = color;
    }

    public Line(long id, String name, String type, String color) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}
