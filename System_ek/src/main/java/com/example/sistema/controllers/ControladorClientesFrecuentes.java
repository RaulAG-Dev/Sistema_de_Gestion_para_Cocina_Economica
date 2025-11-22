package com.example.sistema.controllers;

import com.example.sistema.models.ClienteFrecuente; // 🔑 Usar el nuevo modelo
import com.example.sistema.services.ServicioCliente;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorClientesFrecuentes implements Initializable {

    @FXML private TableView<ClienteFrecuente> frecuentesTable; // 🔑 Cambio de Cliente a ClienteFrecuente
    @FXML private TableColumn<ClienteFrecuente, Integer> rankingColumn;
    @FXML private TableColumn<ClienteFrecuente, String> nombreColumn;
    @FXML private TableColumn<ClienteFrecuente, String> telefonoColumn;
    @FXML private TableColumn<ClienteFrecuente, Long> pedidosColumn; // 🔑 Nueva columna de tipo Long

    private final ServicioCliente servicioCliente = ServicioCliente.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        telefonoColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));

        pedidosColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getCantidadPedidos()));

        rankingColumn.setCellValueFactory(data ->
                new SimpleObjectProperty<>(frecuentesTable.getItems().indexOf(data.getValue()) + 1)
        );

        cargarRankingClientes();
    }

    private void cargarRankingClientes() {
        List<ClienteFrecuente> clientesOrdenados = servicioCliente.obtenerClientesFrecuentes();
        frecuentesTable.getItems().setAll(clientesOrdenados);
    }

    public void manejarCierre(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();

    }
}
