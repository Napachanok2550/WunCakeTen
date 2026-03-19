package com.project;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Entry {
    private String id;
    private String date;     // YYYY-MM-DD
    private String question;
    private String answer;

    public Entry() { } // gson ต้องใช้

    public Entry(LocalDate date, String question, String answer) {
        this.id = UUID.randomUUID().toString();
        this.date = date.toString();
        this.question = question;
        this.answer = answer;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }

    public void setId(String id) { this.id = id; }
    public void setDate(String date) { this.date = date; }
    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry entry)) return false;
        return Objects.equals(id, entry.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}