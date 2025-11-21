package com.example.sistema;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/example/sistema/LogginView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Inicio de sesión a cocina economica");
        stage.setScene(scene);
        stage.show();
    }
}