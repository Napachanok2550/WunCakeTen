package com.project;

import javafx.scene.control.Alert;

public class UIUtil {
    public static void alert(Alert.AlertType type, String header, String content) {
        Alert a = new Alert(type);
        a.setTitle("Ask Yourself");
        a.setHeaderText(header);
        a.setContentText(content);
        a.showAndWait();
    }
}