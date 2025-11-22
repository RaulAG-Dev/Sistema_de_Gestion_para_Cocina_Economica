package com.example.sistema.controllers;

import com.example.sistema.models.Cliente;
import com.example.sistema.models.Pedido; // Necesario para el historial
import com.example.sistema.services.ServicioCliente;
import com.example.sistema.services.ServicioVentas; // Necesario para obtener pedidos
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ControladorGestionClientes implements Initializable {

    @FXML private Button regresarButton;
    @FXML private Button clientesFrecuentesButton;

    @FXML private TableView<Cliente> clientesTable;
    @FXML private TableColumn<Cliente, Integer> idClienteColumn;
    @FXML private TableColumn<Cliente, String> nombreClienteColumn;
    @FXML private TableColumn<Cliente, String> telefonoClienteColumn;
    @FXML private TableColumn<Cliente, Void> accionClienteColumn;

    @FXML private TextField idClienteField;
    @FXML private TextField nombreClienteField;
    @FXML private TextField telefonoClienteField;
    @FXML private TextArea direccionClienteArea;
    @FXML private Button guardarClienteButton;

    @FXML private TableView<Pedido> pedidosClienteTable;
    @FXML private TableColumn<Pedido, Integer> pedidoIdColumn;
    @FXML private TableColumn<Pedido, String> pedidoFechaColumn;
    @FXML private TableColumn<Pedido, Float> pedidoTotalColumn;

    private final ServicioCliente servicioCliente = ServicioCliente.getInstance();
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance(); // 🔑 Instancia del ServicioVentas

    private Cliente clienteActual;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        idClienteColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nombreClienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        telefonoClienteColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
        pedidoIdColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        pedidoTotalColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getTotal()));
        pedidoFechaColumn.setCellValueFactory(data -> {
            java.util.Date fecha = data.getValue().getFechaHora();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yy");
            return new SimpleStringProperty(fecha != null ? sdf.format(fecha) : "N/A");
        });

        configurarColumnaAcciones();
        clientesTable.getItems().setAll(servicioCliente.obtenerTodos());
        clientesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                clienteActual = newSel;
                idClienteField.setText(String.valueOf(newSel.getId()));
                nombreClienteField.setText(newSel.getNombre());
                telefonoClienteField.setText(newSel.getTelefono());
                direccionClienteArea.setText(newSel.getPreferencias());
                cargarHistorialPedidos(newSel);
            }
        });
    }


    private void cargarHistorialPedidos(Cliente cliente) {
        if (cliente == null || cliente.getId() <= 0) {
            pedidosClienteTable.getItems().clear();
            return;
        }

        List<Pedido> todosLosPedidos = servicioVentas.getPedidosMaestros();

        List<Pedido> historial = todosLosPedidos.stream()
                .filter(p -> p.getCliente() != null && p.getCliente().getId() == cliente.getId())
                .collect(Collectors.toList());
        historial.sort(Comparator.comparing(Pedido::getFechaHora, Comparator.nullsLast(Comparator.reverseOrder())));

        pedidosClienteTable.getItems().setAll(historial);
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
                        prepararNuevoCliente(null);
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
                        cargarHistorialPedidos(cliente);
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
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }

    @FXML
    void verClientesFrecuentes(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/ClienteFrecuente.fxml"));
            Parent root = loader.load();
            Stage nuevaStage = new Stage();
            Scene scene = new Scene(root);
            nuevaStage.setScene(scene);
            nuevaStage.setTitle("Clientes frecuentes");
            nuevaStage.show();

        } catch(IOException e) {
            System.err.println("Error al cargar la vista de Corte de Caja.");
            e.printStackTrace();
        }
    }

    @FXML
    void prepararNuevoCliente(ActionEvent event) {
        clienteActual = new Cliente();
        clienteActual.setId(servicioCliente.generarNuevoId());
        idClienteField.setText(String.valueOf(clienteActual.getId()));
        nombreClienteField.clear();
        telefonoClienteField.clear();
        direccionClienteArea.clear();
        pedidosClienteTable.getItems().clear();
    }

    @FXML
    void guardarCliente(ActionEvent event) {
        String nombre = nombreClienteField.getText();
        clienteActual.setNombre(nombre != null ? nombre.trim() : "");

        String telefono = telefonoClienteField.getText();
        clienteActual.setTelefono(telefono != null ? telefono.trim() : "");

        String direccion = direccionClienteArea.getText();
        clienteActual.setPreferencias(direccion != null ? direccion.trim() : "");
    }
}