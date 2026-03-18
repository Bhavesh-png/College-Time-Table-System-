package com.college.model;

import java.io.Serializable;

public class TimetableEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String day;
    private String start;
    private String end;
    private String subject;
    private String faculty;
    private String room;

    public TimetableEntry(String day, String start, String end, String subject, String faculty, String room) {
        this.day = day;
        this.start = start;
        this.end = end;
        this.subject = subject;
        this.faculty = faculty;
        this.room = room;
    }

    // Getters and Setters
    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }
    public String getStart() { return start; }
    public void setStart(String start) { this.start = start; }
    public String getEnd() { return end; }
    public void setEnd(String end) { this.end = end; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getFaculty() { return faculty; }
    public void setFaculty(String faculty) { this.faculty = faculty; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
}
