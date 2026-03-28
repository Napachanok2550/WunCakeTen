package com.project;

import java.time.LocalDate;
import java.util.UUID;

public class Entry {

    private String id;
    private String date;
    private String question;
    private String answer;
    private String mood;

    public Entry(LocalDate date, String question, String answer, String mood) {
        this.id = UUID.randomUUID().toString();
        this.date = date.toString();
        this.question = question;
        this.answer = answer;
        this.mood = mood;
    }

    public Entry() {}

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public String getMood() { return mood; } // ✅

    public void setId(String id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }
    public void setMood(String mood) { this.mood = mood; } // ✅

    public java.time.LocalDate getLocalDate() {
    return java.time.LocalDate.parse(this.date);
}
}