package com.project;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Comparator;
import java.util.List;

public class HistoryController {

    @FXML private TableView<Entry> table;
    @FXML private TableColumn<Entry, String> colDate;
    @FXML private TableColumn<Entry, String> colQuestion;
    @FXML private TableColumn<Entry, String> colAnswer;

    // ✅ เพิ่ม
    @FXML private TableColumn<Entry, String> colMood;

    private final ObservableList<Entry> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDate()));
        colQuestion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getQuestion()));
        colAnswer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAnswer()));

        // ✅ แสดง emoji
        colMood.setCellValueFactory(c -> {
            String mood = c.getValue().getMood();
            return new SimpleStringProperty(mood == null ? "" : mood);
        });

        table.setItems(data);
        reload();
    }

    private void reload() {
        List<Entry> entries = DataService.loadEntries(Session.getUser());
        entries.sort(Comparator.comparing(Entry::getDate).reversed());
        data.setAll(entries);
    }

    @FXML
    private void onBack() {
        App.setScene("today.fxml");
    }

    @FXML
    private void onDelete() {
        Entry selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIUtil.alert(Alert.AlertType.WARNING, "ยังไม่ได้เลือกข้อมูล", "กรุณาเลือกข้อมูลที่ต้องการลบ");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("ยืนยันการลบ");
        confirm.setHeaderText("ต้องการลบรายการนี้ใช่ไหม?");
        confirm.setContentText("วันที่: " + selected.getDate());

        if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK)
            return;

        List<Entry> entries = DataService.loadEntries(Session.getUser());
        entries.removeIf(e -> e.getId().equals(selected.getId()));
        DataService.saveEntries(Session.getUser(), entries);
        reload();
    }

    @FXML
    private void onEdit() {
        Entry selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            UIUtil.alert(Alert.AlertType.WARNING, "ยังไม่ได้เลือกข้อมูล", "กรุณาเลือกข้อมูลที่ต้องการแก้ไข");
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("แก้ไขคำตอบ");
        dialog.setHeaderText("วันที่: " + selected.getDate() + "\nคำถาม: " + selected.getQuestion());

        TextArea area = new TextArea(selected.getAnswer());
        area.setWrapText(true);
        area.setPrefRowCount(8);

        dialog.getDialogPane().setContent(area);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK ? area.getText() : null);

        String newAnswer = dialog.showAndWait().orElse(null);
        if (newAnswer == null)
            return;

        newAnswer = newAnswer.trim();
        if (newAnswer.isEmpty()) {
            UIUtil.alert(Alert.AlertType.WARNING, "คำตอบว่าง", "คำตอบต้องไม่เป็นค่าว่าง");
            return;
        }

        List<Entry> entries = DataService.loadEntries(Session.getUser());
        for (Entry e : entries) {
            if (e.getId().equals(selected.getId())) {
                e.setAnswer(newAnswer);
                break;
            }
        }

        DataService.saveEntries(Session.getUser(), entries);
        reload();
    }
}