package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TodayController {

    @FXML
    private Label dateLabel;
    @FXML
    private Label questionLabel;
    @FXML
    private TextArea answerArea;

    private LocalDate today;
    private String todayQuestion;

    @FXML
    public void initialize() {
        today = LocalDate.now();
        dateLabel.setText("วันที่: " + today);

        List<String> questions = loadQuestions();
        List<String> shuffled = DataService.loadShuffledQuestions(questions);
        int idx = (today.getDayOfYear() - 1) % shuffled.size();
        todayQuestion = shuffled.get(idx);
        questionLabel.setText(todayQuestion);

        // ✅ ใช้ Session
        List<Entry> entries = DataService.loadEntries(Session.getUser());
        Entry existing = findByDate(entries, today.toString());
        if (existing != null)
            answerArea.setText(existing.getAnswer());
    }

    private List<String> loadQuestions() {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/com/project/questions.txt"),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty())
                    out.add(line);
            }
        } catch (Exception ignored) {
        }
        return out;
    }

    private Entry findByDate(List<Entry> entries, String date) {
        for (Entry e : entries) {
            if (date.equals(e.getDate()))
                return e;
        }
        return null;
    }

    @FXML
    private void onSave() {
        String answer = answerArea.getText() == null ? "" : answerArea.getText().trim();
        if (answer.isEmpty()) {
            UIUtil.alert(Alert.AlertType.WARNING, "ยังไม่ได้พิมพ์คำตอบ", "กรุณาพิมพ์คำตอบก่อนบันทึก");
            return;
        }

        List<Entry> entries = DataService.loadEntries(Session.getUser());
        Entry existing = findByDate(entries, today.toString());

        if (existing == null) {
            entries.add(new Entry(today, todayQuestion, answer));
        } else {
            existing.setQuestion(todayQuestion);
            existing.setAnswer(answer);
        }

        DataService.saveEntries(Session.getUser(), entries);
        UIUtil.alert(Alert.AlertType.INFORMATION, "บันทึกสำเร็จ", "บันทึกคำตอบของวันนี้เรียบร้อยแล้ว");
    }

    @FXML
    private void onGoHistory() {
        App.setScene("history.fxml");
    }
}