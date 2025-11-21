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

    // Asumimos que estas columnas están definidas en el FXML
    @FXML private TableColumn<Pedido, String> colNombre;
    @FXML private TableColumn<Pedido, Float> colTotal;
    @FXML private TableColumn<Pedido, String> colFecha;

    private ServicioReportes servicioReportes;

    @FXML
    public void initialize() {
        // Inicializa el servicio, que se conecta al ServicioVentas Singleton
        this.servicioReportes = new ServicioReportes();

        // Configuración básica de las celdas de la tabla (ajustar si usas POJO)
        colNombre.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNombre()));
        colTotal.setCellValueFactory(cellData -> new javafx.beans.property.SimpleFloatProperty(cellData.getValue().getTotal()).asObject());
        // Se asume que el Pedido tiene un getter para formatear la fecha a String
        colFecha.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFechaHora().toString()));

        // Carga el reporte del día actual por defecto al abrir
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }

    private void mostrarReporte(List<Pedido> pedidos) {
        ObservableList<Pedido> datos = FXCollections.observableArrayList(pedidos);
        tablaReportes.setItems(datos);

        // Calcula el total de las ventas usando el método de Pedido
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
            // El usuario selecciona fechas de forma inclusiva, por lo que ajustamos la hora.
            // Inicio: 00:00:00 del día seleccionado
            LocalDateTime inicio = dpFechaInicio.getValue().atStartOfDay();
            // Fin: Final del día seleccionado (23:59:59.999...)
            LocalDateTime fin = dpFechaFin.getValue().atTime(23, 59, 59);

            mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
        } else {
            // Muestra una alerta si faltan fechas
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