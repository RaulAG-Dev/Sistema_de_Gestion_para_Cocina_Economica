package com.example.sistema.controllers;

import com.example.sistema.models.Platillo;
import com.example.sistema.services.ServicioVentas;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorPopularidad implements Initializable {

    @FXML private TableView<Platillo> tablaPopularidad;
    @FXML private TableColumn<Platillo, String> colPlatillo;
    @FXML private TableColumn<Platillo, Integer> colCantidad;
    @FXML private TableColumn<Platillo, Float> colIngresos;

    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;
    @FXML private ComboBox<String> cmbMes;
    @FXML private ComboBox<Integer> cmbAnioMensual;

    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar combo de meses y años
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        cmbMes.setItems(FXCollections.observableArrayList(nombresMeses));

        int anioActual = Year.now().getValue();
        ObservableList<Integer> anios = FXCollections.observableArrayList();
        for (int i = anioActual; i >= anioActual - 4; i--) {
            anios.add(i);
        }
        cmbAnioMensual.setItems(anios);

        // Mostrar ranking completo al iniciar
        cargarRanking(servicioVentas.obtenerRankingPlatillos());
    }

    private void cargarRanking(List<Platillo> ranking) {
        ObservableList<Platillo> datos = FXCollections.observableArrayList(ranking);
        tablaPopularidad.setItems(datos);

        if (datos.isEmpty()) {
            tablaPopularidad.setPlaceholder(new Label("No se encontraron platillos vendidos en este rango."));
        }

        colPlatillo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre()));
        colCantidad.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCantidadVendida()));
        colIngresos.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getIngresosGenerados()));
    }

    @FXML
    void generarReporteMensualSeleccionado(ActionEvent event) {
        String mesSeleccionadoNombre = cmbMes.getSelectionModel().getSelectedItem();
        Integer anioSeleccionado = cmbAnioMensual.getSelectionModel().getSelectedItem();

        if (mesSeleccionadoNombre == null || anioSeleccionado == null) {
            return; // puedes mostrar alerta si quieres
        }

        int mesNumero = cmbMes.getSelectionModel().getSelectedIndex() + 1;
        LocalDate inicioMes = LocalDate.of(anioSeleccionado, mesNumero, 1);
        LocalDate finMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());

        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fin = finMes.atTime(23, 59, 59);

        cargarRanking(servicioVentas.obtenerRankingPlatillosPorFechas(inicio, fin));
    }

    @FXML
    void generarReportePersonalizado(ActionEvent event) {
        if (dpFechaInicio.getValue() != null) {
            LocalDate inicioDia = dpFechaInicio.getValue();
            LocalDateTime inicio = inicioDia.atStartOfDay();
            LocalDateTime fin = inicioDia.atTime(23, 59, 59);

            // Si también hay fecha final, úsala
            if (dpFechaFin.getValue() != null) {
                fin = dpFechaFin.getValue().atTime(23, 59, 59);
            }
            cargarRanking(servicioVentas.obtenerRankingPlatillosPorFechas(inicio, fin));
        }
    }

    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }
}
