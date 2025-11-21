package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
import com.example.sistema.services.ServicioReportes;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
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

        // 1. Inicializar ComboBox de Meses
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        cmbMes.setItems(FXCollections.observableArrayList(nombresMeses));

        // 2. Inicializar ComboBox de Años (últimos 5 años)
        int anioActual = Year.now().getValue();
        ObservableList<Integer> anios = FXCollections.observableArrayList();
        for (int i = anioActual; i >= anioActual - 4; i--) {
            anios.add(i);
        }
        cmbAnioMensual.setItems(anios);

        // Seleccionar el mes y año actual por defecto
        cmbMes.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        cmbAnioMensual.getSelectionModel().selectFirst();


        // 3. Configuración de las columnas de la tabla
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colTotal.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotal()).asObject());
        // Asumiendo que getFechaHora() devuelve un java.util.Date, convertimos a String
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHora().toString()));

        // Cargar reporte de hoy al iniciar
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
        // Ocultar selector mensual si estaba visible
        monthlySelectionBox.setVisible(false);
        monthlySelectionBox.setManaged(false);
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }

    // 🔑 Mantenemos el método para un uso futuro o si lo necesitas en el FXML
    @FXML
    void generarReporteSemanal(ActionEvent event) {
        mostrarAlerta("Lógica no implementada", "Por favor, usa el filtro de Hoy, Mensual o el Personalizado.");
    }

    // 🔑 Mantenemos el método que genera el reporte del mes actual (rápido)
    @FXML
    void generarReporteMensual(ActionEvent event) {
        // La lógica original para generar reporte mensual rápido
        mostrarReporte(servicioReportes.obtenerReporteMensual());
    }

    // -----------------------------------------------------------------
    // LÓGICA DE SELECCIÓN POR COMBOBOX
    // -----------------------------------------------------------------

    /**
     * Muestra los ComboBox de selección de Mes/Año.
     */
    @FXML
    private void showMonthlySelection(ActionEvent event) {
        monthlySelectionBox.setVisible(true);
        monthlySelectionBox.setManaged(true);
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

        // Convertir el nombre del mes a un número (1=Enero, 12=Diciembre)
        int mesNumero = cmbMes.getSelectionModel().getSelectedIndex() + 1;

        // 1. Definir el rango de fechas para el mes seleccionado
        LocalDate inicioMes = LocalDate.of(anioSeleccionado, mesNumero, 1);
        LocalDate finMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());

        // 2. Ajustar a LocalDateTime para el servicio
        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fin = finMes.atTime(23, 59, 59); // Incluir el final del día

        // 3. Generar y mostrar el reporte
        mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
    }
    // -----------------------------------------------------------------

    @FXML
    void generarReportePersonalizado(ActionEvent event) {
        if (dpFechaInicio.getValue() != null && dpFechaFin.getValue() != null) {
            // Ocultar selector mensual si estaba visible
            monthlySelectionBox.setVisible(false);
            monthlySelectionBox.setManaged(false);

            // Se ajusta la hora para incluir el día completo
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
}