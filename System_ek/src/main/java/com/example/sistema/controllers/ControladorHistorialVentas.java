package com.example.sistema.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

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
        //para regresar a la vista principal

        System.out.println("Botón 'Regresar' presionado.");
    }


    @FXML
    void reimprimirTicket(ActionEvent event) {
        // con este deberias reinprimir el ticket
        //  debes de obtener la venta seleccionada de 'ventasTable'.


        System.out.println("Botón 'Reimprimir Ticket' presionado.");
    }
}