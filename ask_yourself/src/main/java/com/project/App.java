package com.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        stage.setTitle("Ask Yourself - Daily Reflection");
        setScene("login.fxml");
        stage.show();
    }

    public static void setScene(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/project/" + fxml));
            Scene scene = new Scene(loader.load(), 900, 600);

            var cssUrl = App.class.getResource("/com/project/app.css");
            if (cssUrl != null)
                scene.getStylesheets().add(cssUrl.toExternalForm());

            primaryStage.setScene(scene);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load scene: " + fxml, e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}