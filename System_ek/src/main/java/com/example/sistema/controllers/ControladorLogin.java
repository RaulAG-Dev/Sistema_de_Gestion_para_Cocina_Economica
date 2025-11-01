package com.example.sistema.controllers;

import com.example.sistema.models.Usuario;
import com.example.sistema.services.ServicioUsuarios;
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
    private ServicioUsuarios servicioUsuarios;

    public ControladorLogin(){
        this.servicioUsuarios = new ServicioUsuarios();
    }

    @FXML
    protected void manejarLogin(ActionEvent event) {
        String usuario = usuarioField.getText().trim();
        String password = passField.getText().trim();

       try{
           Usuario usuario1 = servicioUsuarios.autenticar(usuario, password);
           cambiarEscena();
       }catch (Exception e){
           mensajeError.setText(e.getMessage());
       }
    }

    private void cambiarEscena() {
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
    }
}