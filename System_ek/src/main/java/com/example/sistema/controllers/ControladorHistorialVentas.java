package com.example.sistema.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;



public class ControladorHistorialVentas {

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


        System.out.println("ControladorHistorialVentas inicializado.");
    }


}