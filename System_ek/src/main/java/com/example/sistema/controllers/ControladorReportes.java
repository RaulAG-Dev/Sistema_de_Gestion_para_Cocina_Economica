package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
import com.example.sistema.models.Platillo;
import com.example.sistema.services.ServicioReportes;
import com.example.sistema.services.ServicioVentas;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;


public class ControladorReportes implements Initializable {

    @FXML private TableView<Pedido> tablaReportes;
    @FXML private Label montoTotalLabel;
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    @FXML private TableColumn<Pedido, String> colNombre;
    @FXML private TableColumn<Pedido, Float> colTotal;
    @FXML private TableColumn<Pedido, String> colFecha;

    @FXML private HBox monthlySelectionBox;
    @FXML private ComboBox<String> cmbMes;
    @FXML private ComboBox<Integer> cmbAnioMensual;

    private ServicioReportes servicioReportes;
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.servicioReportes = new ServicioReportes();
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        cmbMes.setItems(FXCollections.observableArrayList(nombresMeses));
        int anioActual = Year.now().getValue();
        ObservableList<Integer> anios = FXCollections.observableArrayList();

        for (int i = anioActual; i >= anioActual - 4; i--) {
            anios.add(i);
        }
        cmbAnioMensual.setItems(anios);
        cmbMes.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        cmbAnioMensual.getSelectionModel().selectFirst();
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colTotal.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotal()).asObject());
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHora().toString()));
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }

    private void mostrarReporte(List<Pedido> pedidos) {
        ObservableList<Pedido> datos = FXCollections.observableArrayList(pedidos);
        tablaReportes.setItems(datos);

        double total = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
        montoTotalLabel.setText(currencyFormatter.format(total));
    }

    @FXML
    void generarReporteHoy(ActionEvent event) {
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }


    /**
     * Genera el reporte basado en la selección de mes y año de los ComboBox.
     */
    @FXML
    private void generarReporteMensualSeleccionado(ActionEvent event) {
        String mesSeleccionadoNombre = cmbMes.getSelectionModel().getSelectedItem();
        Integer anioSeleccionado = cmbAnioMensual.getSelectionModel().getSelectedItem();

        if (mesSeleccionadoNombre == null || anioSeleccionado == null) {
            mostrarAlerta("Error de Selección", "Por favor, selecciona tanto el Mes como el Año.");
            return;
        }
        int mesNumero = cmbMes.getSelectionModel().getSelectedIndex() + 1;

        LocalDate inicioMes = LocalDate.of(anioSeleccionado, mesNumero, 1);
        LocalDate finMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fin = finMes.atTime(23, 59, 59);

        mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
    }

    @FXML
    void generarReportePersonalizado(ActionEvent event) {
        if (dpFechaInicio.getValue() != null && dpFechaFin.getValue() != null) {
            LocalDateTime inicio = dpFechaInicio.getValue().atStartOfDay();
            LocalDateTime fin = dpFechaFin.getValue().atTime(23, 59, 59);

            mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
        } else {
            mostrarAlerta("Error", "Debes seleccionar las fechas de inicio y fin.");
        }
    }

    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    void mostrarPopularidad(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/Popularidad.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Platillos Populares");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de popularidad.");
        }
    }

}