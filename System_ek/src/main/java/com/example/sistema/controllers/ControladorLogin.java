package com.example.sistema.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControladorLogin {


    @FXML
    private TextField usuarioField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button loginButton;

    @FXML
    private Label mensajeError;

    @FXML
    protected void manejarLogin(ActionEvent event) {
        String usuario = usuarioField.getText();
        String password = passField.getText();

        // Emiliano o  michelle esta es la lógica de autenticación temporal
        //aqui es donde se concecta con la base de datos (usuario, contraseña)

        if (usuario.equals("admin") && password.equals("1234")) {
            System.out.println("¡Acceso concedido! Cargando vista principal...");
            mensajeError.setText("");

            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/principal-view.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) loginButton.getScene().getWindow();

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Gestión Cocina Económica - Principal");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Acceso denegado.");
            mensajeError.setText("Usuario o contraseña incorrectos.");
        }
    }

    @FXML
    public void initialize() {
        mensajeError.setText("");
    }
}