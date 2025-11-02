package com.example.sistema.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorInventario implements Initializable {

    @FXML private Button regresarButton;
    @FXML private TableView<?> inventarioTable;
    @FXML private TextArea notificacionesArea;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    void manejarRegreso(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/principal-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) regresarButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}