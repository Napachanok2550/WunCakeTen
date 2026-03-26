package com.project;

import java.time.LocalDate;
import java.util.UUID;

public class Entry {

    private String id;
    private String date;
    private String question;
    private String answer;

    // Constructor สำหรับสร้างใหม่
    public Entry(LocalDate date, String question, String answer) {
        this.id = UUID.randomUUID().toString(); // สร้าง id ไม่ซ้ำ
        this.date = date.toString();
        this.question = question;
        this.answer = answer;
    }

    // Constructor ว่าง (จำเป็นสำหรับ Gson)
    public Entry() {}

    // Getter
    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    // Setter
    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}