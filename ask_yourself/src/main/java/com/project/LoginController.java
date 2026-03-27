@FXML
private void onLogin() {
    String username = usernameField.getText();
    String password = passwordField.getText();

    if (!validateInput(username, password))
        return;

    List<User> users = DataService.loadUsers();
    for (User u : users) {
        if (u.getUsername().equals(username) &&
                u.getPasswordHash().equals(DataService.hashPassword(password))) {

            Session.setUser(username); // ✅ ใช้ Session
            App.setScene("today.fxml");
            return;
        }
    }
    errorLabel.setText("Username หรือ Password ไม่ถูกต้อง");
}