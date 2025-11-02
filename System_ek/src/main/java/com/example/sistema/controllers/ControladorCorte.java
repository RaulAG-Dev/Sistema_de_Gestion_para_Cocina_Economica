package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControladorCorte implements Initializable {

    @FXML private Button regresarButton;
    @FXML private TableView<Pedido> ventasTable;
    @FXML private TableColumn<Pedido, Integer> numVentaColumn;
    @FXML private TableColumn<Pedido, String> detalleVentaColumn;
    @FXML private TableColumn<Pedido, Float> totalVentaColumn;
    @FXML private Label totalCorteLabel;
    @FXML private DatePicker fechaCortePicker;
    @FXML private Button guardarButton;
    @FXML private Button imprimirButton;
    @FXML private TextArea notasArea;


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

    @FXML
    private void guardarCorte() {
        System.out.println("Guardando corte para la fecha: " + fechaCortePicker.getValue());
    }

    @FXML
    private void imprimirCorte() {

        System.out.println("Imprimiendo resumen del corte.");
    }

}