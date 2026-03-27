package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class TodayController {

    @FXML private Label dateLabel;
    @FXML private Label questionLabel;
    @FXML private TextArea answerArea;

    // ✅ emoji mood
    @FXML private ToggleGroup moodGroup;
    private String selectedMood = "😐"; // default

    private LocalDate today;
    private String todayQuestion;

    @FXML
    public void initialize() {
        today = LocalDate.now();
        dateLabel.setText("วันที่: " + today);

        List<String> questions = loadQuestions();

        // ✅ สุ่มแบบไม่ซ้ำทั้งปี
        int year = today.getYear();
        Random random = new Random(year);
        Collections.shuffle(questions, random);

        int idx = (today.getDayOfYear() - 1) % questions.size();
        todayQuestion = questions.get(idx);
        questionLabel.setText(todayQuestion);

        // debug (ลบได้)
        System.out.println("Total questions: " + questions.size());
        System.out.println("Today question: " + todayQuestion);

        // โหลดคำตอบเดิม
        List<Entry> entries = DataService.loadEntries(Session.getUser());
        Entry existing = findByDate(entries, today.toString());

        if (existing != null) {
            answerArea.setText(existing.getAnswer());

            // โหลด mood เดิม
            if (existing.getMood() != null) {
                selectedMood = existing.getMood();
                setSelectedMoodInUI(selectedMood);
            }
        }
    }

    // -----------------------
    // load questions
    // -----------------------
    private List<String> loadQuestions() {
        List<String> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/com/project/questions.txt"),
                        StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) out.add(line);
            }
        } catch (Exception ignored) {}
        return out;
    }

    // -----------------------
    // find entry by date
    // -----------------------
    private Entry findByDate(List<Entry> entries, String date) {
        for (Entry e : entries) {
            if (date.equals(e.getDate())) return e;
        }
        return null;
    }

    // -----------------------
    // emoji click
    // -----------------------
    @FXML
    private void onSelectMood() {
        Toggle selected = moodGroup.getSelectedToggle();
        if (selected != null) {
            selectedMood = ((RadioButton) selected).getText();
        }
    }

    // set mood จากข้อมูลเก่า
    private void setSelectedMoodInUI(String mood) {
        for (Toggle t : moodGroup.getToggles()) {
            RadioButton rb = (RadioButton) t;
            if (rb.getText().equals(mood)) {
                rb.setSelected(true);
                break;
            }
        }
    }

    // -----------------------
    // save
    // -----------------------
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
            entries.add(new Entry(today, todayQuestion, answer, selectedMood)); // ✅ mood
        } else {
            existing.setQuestion(todayQuestion);
            existing.setAnswer(answer);
            existing.setMood(selectedMood); // ✅ update mood
        }

        DataService.saveEntries(Session.getUser(), entries);

        UIUtil.alert(Alert.AlertType.INFORMATION, "บันทึกสำเร็จ", "บันทึกคำตอบของวันนี้เรียบร้อยแล้ว");
    }

    // -----------------------
    // go history
    // -----------------------
    @FXML
    private void onGoHistory() {
        App.setScene("history.fxml");
    }
}