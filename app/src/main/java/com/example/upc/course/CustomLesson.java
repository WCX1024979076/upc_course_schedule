package com.example.upc.course;


public class CustomLesson extends Lesson {

    private String teacher;
    private String identifier;
    public CustomLesson(String term, String week, String name, String weekday, int start, int end, String place, String teacher, String date, String identifier) {
        super(term, week, name, weekday, start, end, place,date);
        this.teacher = teacher;
        this.identifier = identifier;
    }
    public CustomLesson(String term, String week, String name, String weekday, int start, int end, String place,String identifier,int color,String date) {
        super(term, week, name, weekday, start, end, place,color,date);
        this.identifier = identifier;
    }

    public String getTeacher() {
        return teacher;
    }


    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return getTerm()+getWeek()+getName()+getWeekday()+getStart()+getEnd()+getPlace()+teacher+identifier;
    }
}
