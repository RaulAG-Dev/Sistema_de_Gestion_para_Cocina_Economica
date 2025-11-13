package com.example.sistema.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

//hola juan calos

public class ControladorHistorialVentas {
// en los privates debes de cmabiar lo que esta dentro de los ¿? por los modelos corresponientes
    @FXML
    private Button regresarButton;

    @FXML
    private TableView<?> ventasTable;

    @FXML
    private TableColumn<?, ?> idVentaColumn;

    @FXML
    private TableColumn<?, ?> fechaColumn;

    @FXML
    private TableColumn<?, ?> clienteColumn;

    @FXML
    private TableColumn<?, ?> totalColumn;

    @FXML
    private TextArea detallesVentaArea;

    @FXML
    private Button reimprimirButton;


    @FXML
    public void initialize() {
        // Lógica de tu compañero:
        // 1. Configurar las columnas (setCellValueFactory).
        // 2. Cargar los datos de las ventas en 'ventasTable'.
        // 3. Añadir listener a 'ventasTable' para mostrar detalles en 'detallesVentaArea'.

        System.out.println("ControladorHistorialVentas inicializado.");
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
            System.err.println("Error al cargar la vista principal.");
            e.printStackTrace();
        }
    }


    @FXML
    void reimprimirTicket(ActionEvent event) {
        // con este deberias reinprimir el ticket
        //  debes de obtener la venta seleccionada de 'ventasTable'.


        System.out.println("Botón 'Reimprimir Ticket' presionado.");
    }
}