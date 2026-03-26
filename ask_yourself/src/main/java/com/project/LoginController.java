package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // ✅ Validation
        if (!validateInput(username, password))
            return;

        // 🔐 mock login (กำหนดเองก่อน)
        if (username.equals("admin") && password.equals("1234")) {
            App.setScene("today.fxml");
        } else {
            errorLabel.setText("Username หรือ Password ไม่ถูกต้อง");
        }
    }

    private boolean validateInput(String username, String password) {

        // 1. ห้ามว่าง
        if (username == null || username.trim().isEmpty()) {
            errorLabel.setText("กรุณากรอก Username");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            errorLabel.setText("กรุณากรอก Password");
            return false;
        }

        // 2. Username ต้องเป็นตัวอักษร/ตัวเลขเท่านั้น
        if (!username.matches("^[a-zA-Z0-9]+$")) {
            errorLabel.setText("Username ต้องเป็น a-z หรือ 0-9 เท่านั้น");
            return false;
        }

        // 3. Password อย่างน้อย 4 ตัว
        if (password.length() < 4) {
            errorLabel.setText("Password ต้องมีอย่างน้อย 4 ตัว");
            return false;
        }

        return true;
    }
}