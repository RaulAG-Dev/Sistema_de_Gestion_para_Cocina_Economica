package com.example.sistema.controllers;

import com.example.sistema.models.Cliente;
import com.example.sistema.services.ServicioCliente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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


    @FXML private TableView<Cliente> clientesTable; // Reemplazar '?' por Cliente
    @FXML private TableColumn<Cliente, Integer> idClienteColumn; // Reemplazar '?'
    @FXML private TableColumn<Cliente, String> nombreClienteColumn; // Reemplazar '?'
    @FXML private TableColumn<Cliente, String> telefonoClienteColumn; // Reemplazar '?'
    @FXML private TableColumn<Cliente, Void> accionClienteColumn; // <- NUEVA COLUMNA



    @FXML private TextField idClienteField;
    @FXML private TextField nombreClienteField;
    @FXML private TextField telefonoClienteField;
    @FXML private TextArea direccionClienteArea;
    @FXML private Button guardarClienteButton;

    private ServicioCliente servicioCliente;
    private Cliente clienteActual;


    @FXML
    public void initialize() {
        servicioCliente = new ServicioCliente();

        idClienteColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nombreClienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        telefonoClienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));

        configurarColumnaAcciones();
        clientesTable.getItems().setAll(servicioCliente.obtenerTodos());

        // Listener para cargar cliente en formulario
        clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                clienteActual = newSel;
                idClienteField.setText(String.valueOf(newSel.getId()));
                nombreClienteField.setText(newSel.getNombre());
                telefonoClienteField.setText(newSel.getTelefono());
                direccionClienteArea.setText(newSel.getPreferencias());
            }
        });
    }



    private void configurarColumnaAcciones() {
        accionClienteColumn.setCellFactory(col -> new TableCell<>() {
            private final Button eliminarBtn = new Button("❌");
            private final Button editarBtn = new Button("...");

            {
                eliminarBtn.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente != null) {
                        servicioCliente.eliminar(cliente.getId());
                        clientesTable.getItems().setAll(servicioCliente.obtenerTodos());
                        prepararNuevoCliente(null); // limpiar formulario si se estaba editando
                    }
                });

                editarBtn.setOnAction(e -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    if (cliente != null) {
                        clienteActual = cliente;
                        idClienteField.setText(String.valueOf(cliente.getId()));
                        nombreClienteField.setText(cliente.getNombre());
                        telefonoClienteField.setText(cliente.getTelefono());
                        direccionClienteArea.setText(cliente.getPreferencias());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(editarBtn, eliminarBtn);
                    box.setSpacing(5);
                    setGraphic(box);
                }
            }
        });
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
        clienteActual = new Cliente();
        clienteActual.setId(servicioCliente.generarNuevoId());
        idClienteField.setText(String.valueOf(clienteActual.getId()));
        nombreClienteField.clear();
        telefonoClienteField.clear();
        direccionClienteArea.clear();
    }

    @FXML
    void guardarCliente(ActionEvent event) {
        if (clienteActual == null) {
            clienteActual = new Cliente();
            clienteActual.setId(servicioCliente.generarNuevoId());
        }
        clienteActual.setNombre(nombreClienteField.getText().trim());
        clienteActual.setTelefono(telefonoClienteField.getText().trim());
        clienteActual.setPreferencias(direccionClienteArea.getText().trim());

        servicioCliente.guardar(clienteActual);
        clientesTable.getItems().setAll(servicioCliente.obtenerTodos());
        prepararNuevoCliente(null);
    }



}