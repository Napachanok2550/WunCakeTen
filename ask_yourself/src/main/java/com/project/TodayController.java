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

    @FXML private Label dateLabel;
    @FXML private Label questionLabel;
    @FXML private TextArea answerArea;

    private LocalDate today;
    private String todayQuestion;

    @FXML
    public void initialize() {
        today = LocalDate.now();
        setupDate();
        setupQuestion();
        loadExistingAnswer();
    }

    //Setup Methods//

    private void setupDate() {
        dateLabel.setText("วันที่: " + today);
    }

    private void setupQuestion() {
        List<String> questions = loadQuestions();

        if (questions.isEmpty()) {
            todayQuestion = getDefaultQuestion();
        } else {
            todayQuestion = getQuestionOfTheDay(questions);
        }

        questionLabel.setText(todayQuestion);
    }

    private void loadExistingAnswer() {
        List<Entry> entries = DataService.loadEntries();
        Entry existing = findByDate(entries, today.toString());

        if (existing != null) {
            answerArea.setText(existing.getAnswer());
        }
    }

    //Logic Methods//

    private String getQuestionOfTheDay(List<String> questions) {
        List<String> shuffled = DataService.loadShuffledQuestions(questions);

        if (shuffled.isEmpty()) {
            return getDefaultQuestion();
        }

        int index = (today.getDayOfYear() - 1) % shuffled.size();
        return shuffled.get(index);
    }

    private String getDefaultQuestion() {
        return "วันนี้คุณรู้สึกอย่างไร?";
    }

    private Entry findByDate(List<Entry> entries, String date) {
        return entries.stream()
                .filter(e -> date.equals(e.getDate()))
                .findFirst()
                .orElse(null);
    }

    //Load Questions//


    private List<String> loadQuestions() {
        List<String> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(
                        getClass().getResourceAsStream("/com/project/questions.txt"),
                        StandardCharsets.UTF_8))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    result.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    //Actions//

    @FXML
    private void onSave() {
        String answer = answerArea.getText() == null ? "" : answerArea.getText().trim();

        if (answer.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,
                    "ยังไม่ได้พิมพ์คำตอบ",
                    "กรุณาพิมพ์คำตอบก่อนบันทึก");
            return;
        }

        List<Entry> entries = DataService.loadEntries();
        Entry existing = findByDate(entries, today.toString());

        if (existing == null) {
            entries.add(new Entry(today, todayQuestion, answer));
        } else {
            existing.setQuestion(todayQuestion);
            existing.setAnswer(answer);
        }

        DataService.saveEntries(entries);

        showAlert(Alert.AlertType.INFORMATION,
                "บันทึกสำเร็จ",
                "บันทึกคำตอบของวันนี้เรียบร้อยแล้ว");
    }

    @FXML
    private void onGoHistory() {
        App.setScene("history.fxml");
    }

    //Alert Helper//

    private void showAlert(Alert.AlertType type, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle("Ask Yourself");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}