package com.example.sistema.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;


public class ControladorGestionClientes {


    @FXML private Button regresarButton;
    @FXML private Button clientesFrecuentesButton;


    @FXML private TableView<?> clientesTable; // Reemplazar '?' por Cliente
    @FXML private TableColumn<?, ?> idClienteColumn; // Reemplazar '?'
    @FXML private TableColumn<?, ?> nombreClienteColumn; // Reemplazar '?'
    @FXML private TableColumn<?, ?> telefonoClienteColumn; // Reemplazar '?'
    @FXML private TableColumn<?, ?> accionClienteColumn; // <- NUEVA COLUMNA


    @FXML private Button addClienteButton;
    @FXML private TextField idClienteField;
    @FXML private TextField nombreClienteField;
    @FXML private TextField telefonoClienteField;
    @FXML private TextArea direccionClienteArea;
    @FXML private Button guardarClienteButton;


    @FXML
    public void initialize() {

    }


    private void configurarColumnaAcciones() {

    }


    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Stage stageActual = (Stage) scene.getWindow();
        stageActual.close();
    }

    @FXML
    void verClientesFrecuentes(ActionEvent event) {
        System.out.println("Ver Clientes Frecuentes");
    }

    @FXML
    void prepararNuevoCliente(ActionEvent event) {
        System.out.println("Formulario limpiado para nuevo cliente");
    }

    @FXML
    void guardarCliente(ActionEvent event) {
        System.out.println("Guardar Cliente");
    }


}