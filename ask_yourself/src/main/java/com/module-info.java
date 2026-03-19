module com.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens com.project to javafx.fxml, com.google.gson;
    exports com.project;
}