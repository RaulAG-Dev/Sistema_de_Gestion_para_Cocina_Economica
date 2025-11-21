package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
import com.example.sistema.services.ServicioReportes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.List;

public class ControladorReportes {

    @FXML private TableView<Pedido> tablaReportes;
    @FXML private Label labelTotalVentas;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    @FXML private TableColumn<Pedido, String> colNombre;
    @FXML private TableColumn<Pedido, Float> colTotal;
    @FXML private TableColumn<Pedido, String> colFecha;

    private ServicioReportes servicioReportes;

    @FXML
    public void initialize() {
        this.servicioReportes = new ServicioReportes();
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colTotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getTotal()).asObject());
        colFecha.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaHora().toString()));
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }

    private void mostrarReporte(List<Pedido> pedidos) {
        ObservableList<Pedido> datos = FXCollections.observableArrayList(pedidos);
        tablaReportes.setItems(datos);
        double total = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
        labelTotalVentas.setText(String.format("Total de Ventas: $%.2f", total));
    }

    @FXML
    void generarReporteHoy(ActionEvent event) {
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }

    @FXML
    void generarReporteSemanal(ActionEvent event) {
        mostrarReporte(servicioReportes.obtenerReporteSemanal());
    }

    @FXML
    void generarReporteMensual(ActionEvent event) {
        mostrarReporte(servicioReportes.obtenerReporteMensual());
    }

    @FXML
    void generarReportePersonalizado(ActionEvent event) {
        if (dpFechaInicio.getValue() != null && dpFechaFin.getValue() != null) {
            LocalDateTime inicio = dpFechaInicio.getValue().atStartOfDay();
            LocalDateTime fin = dpFechaFin.getValue().atTime(23, 59, 59);

            mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Debes seleccionar las fechas de inicio y fin.", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Stage stageActual = (Stage) scene.getWindow();
        stageActual.close();
    }
}