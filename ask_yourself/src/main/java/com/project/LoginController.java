package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (!validateInput(username, password)) return;

        List<User> users = DataService.loadUsers();

        for (User u : users) {
            if (u.getUsername().equals(username) &&
                u.getPasswordHash().equals(DataService.hashPassword(password))) {

                Session.setUser(username); // ✅ ใช้ Session แทน App.currentUser
                App.setScene("today.fxml");
                return;
            }
        }

        errorLabel.setText("Username หรือ Password ไม่ถูกต้อง");
    }

    private boolean validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            errorLabel.setText("กรุณากรอก Username");
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            errorLabel.setText("กรุณากรอก Password");
            return false;
        }

        if (!username.matches("^[a-zA-Z0-9]+$")) {
            errorLabel.setText("Username ต้องเป็น a-z หรือ 0-9 เท่านั้น");
            return false;
        }

        if (password.length() < 4) {
            errorLabel.setText("Password ต้องมีอย่างน้อย 4 ตัว");
            return false;
        }

        errorLabel.setText(""); // ✅ เคลียร์ error เมื่อ valid
        return true;
    }
}