package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class WeeklyController {

    @FXML
    private Label weekLabel;
    @FXML
    private Label summaryLabel;
    @FXML
    private Label moodBreakdownLabel;

    @FXML
    public void initialize() {
        List<Entry> entries = DataService.loadEntries(Session.getUser());

        LocalDate today = LocalDate.now();
        int currentWeek = today.get(WeekFields.of(Locale.getDefault()).weekOfYear());

        Map<String, Integer> moodCount = new HashMap<>();

        for (Entry e : entries) {
            LocalDate d = e.getLocalDate();
            int week = d.get(WeekFields.of(Locale.getDefault()).weekOfYear());

            if (week == currentWeek) {
                String mood = e.getMood();
                if (mood != null) {
                    moodCount.put(mood, moodCount.getOrDefault(mood, 0) + 1);
                }
            }
        }

        weekLabel.setText("สัปดาห์ที่ " + currentWeek);

        if (moodCount.isEmpty()) {
            summaryLabel.setText("ยังไม่มีข้อมูลในสัปดาห์นี้");
            moodBreakdownLabel.setText("-");
            return;
        }

        // หา mood ที่มากที่สุด
        String topMood = null;
        int max = 0;

        StringBuilder breakdown = new StringBuilder();

        for (Map.Entry<String, Integer> entry : moodCount.entrySet()) {
            breakdown.append(entry.getKey()).append(" = ").append(entry.getValue()).append("\n");

            if (entry.getValue() > max) {
                max = entry.getValue();
                topMood = entry.getKey();
            }
        }

        summaryLabel.setText("อารมณ์หลักของสัปดาห์: " + topMood);
        moodBreakdownLabel.setText(breakdown.toString());
    }

    @FXML
    private void onBack() {
        App.setScene("today.fxml");
    }
}