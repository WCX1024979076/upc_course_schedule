package com.example.upc.course;

public class Lesson {

    private String term;
    private String week;
    private String name;
    private String weekday;
    private int start;
    private int end;
    private String place;
    private int color;
    private String date;
    public String getTerm() {
        return term;
    }

    public String getWeek() {
        return week;
    }

    public String getName() {
        return name;
    }

    public String getWeekday() {
        return weekday;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }
    public int getColor() { return color;}

    public Lesson(String term, String week, String name, String weekday, int start, int end, String place,String date) {
        this.term = term;
        this.week = week;
        this.name = name;
        this.weekday = weekday;
        this.start = start;
        this.end = end;
        this.place = place;
        this.date=date;
    }
    public Lesson(String term, String week, String name, String weekday, int start, int end, String place,int color,String date) {
        this.term = term;
        this.week = week;
        this.name = name;
        this.weekday = weekday;
        this.start = start;
        this.end = end;
        this.place = place;
        this.color=color;
        this.date=date;
    }
    @Override
    public String toString() {
        return term+week+name+place;
    }
}
