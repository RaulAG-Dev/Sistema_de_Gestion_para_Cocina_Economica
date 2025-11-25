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

/**
 * **Controlador de la vista para la generación y visualización de reportes de ventas.**
 * <p>
 * Permite a los usuarios filtrar y visualizar el listado de {@link Pedido}s
 * y calcular el monto total de ventas para rangos de tiempo predefinidos (hoy, mensual)
 * o personalizados. También proporciona acceso a la vista de popularidad de platillos.
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 3.0
 * @since 2025-11-23
 */
public class ControladorReportes implements Initializable {

    // --- Componentes FXML de la Vista ---
    /** Tabla que muestra los pedidos que cumplen con el filtro de fecha. */
    @FXML private TableView<Pedido> tablaReportes;
    /** Etiqueta que muestra la suma total de los pedidos mostrados en la tabla. */
    @FXML private Label montoTotalLabel;
    // Filtro personalizado
    @FXML private DatePicker dpFechaInicio;
    @FXML private DatePicker dpFechaFin;

    // Columnas de la tabla
    @FXML private TableColumn<Pedido, String> colNombre;
    @FXML private TableColumn<Pedido, Float> colTotal;
    @FXML private TableColumn<Pedido, String> colFecha;

    // Filtro mensual
    @FXML private HBox monthlySelectionBox;
    @FXML private ComboBox<String> cmbMes;
    @FXML private ComboBox<Integer> cmbAnioMensual;

    // --- Servicios y Utilidades ---
    private ServicioReportes servicioReportes;
    /** Formato de moneda específico de la región (e.g., pesos mexicanos). */
    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));

    /**
     * Inicializa el controlador:
     * 1. Inicializa el {@link ServicioReportes}.
     * 2. Configura los ComboBox de meses y años.
     * 3. Configura las fábricas de celdas de la tabla.
     * 4. Muestra el reporte de ventas del día actual por defecto.
     *
     * @param url La ubicación utilizada para resolver rutas relativas.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     * @Override
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.servicioReportes = new ServicioReportes();
        // Inicializar ComboBox de meses
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        cmbMes.setItems(FXCollections.observableArrayList(nombresMeses));
        // Inicializar ComboBox de años (últimos 5 años, incluyendo el actual)
        int anioActual = Year.now().getValue();
        ObservableList<Integer> anios = FXCollections.observableArrayList();

        for (int i = anioActual; i >= anioActual - 4; i--) {
            anios.add(i);
        }
        cmbAnioMensual.setItems(anios);
        // Seleccionar por defecto el mes y año actuales
        cmbMes.getSelectionModel().select(LocalDate.now().getMonthValue() - 1);
        cmbAnioMensual.getSelectionModel().selectFirst();
        // Configuración de Cell Value Factories
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colTotal.setCellValueFactory(cellData -> new SimpleFloatProperty(cellData.getValue().getTotal()).asObject());
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHora().toString()));
        // Carga inicial del reporte de hoy
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }

    /**
     * Actualiza la tabla de reportes con la lista de pedidos proporcionada
     * y recalcula el monto total de ventas para ese listado.
     *
     * @param pedidos La lista de {@link Pedido} a mostrar.
     */
    private void mostrarReporte(List<Pedido> pedidos) {
        ObservableList<Pedido> datos = FXCollections.observableArrayList(pedidos);
        tablaReportes.setItems(datos);

        // Calcular el total
        double total = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
        montoTotalLabel.setText(currencyFormatter.format(total));
    }

    /**
     * Genera y muestra el reporte de ventas correspondiente al día de hoy.
     *
     * @param event El evento de acción.
     */
    @FXML
    void generarReporteHoy(ActionEvent event) {
        mostrarReporte(servicioReportes.obtenerReporteHoy());
    }


    /**
     * Genera el reporte basado en la selección de mes y año de los ComboBox.
     *
     * @param event El evento de acción.
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

        // Calcular el rango del mes
        LocalDate inicioMes = LocalDate.of(anioSeleccionado, mesNumero, 1);
        LocalDate finMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());
        LocalDateTime inicio = inicioMes.atStartOfDay();
        // Usar hora de inicio (00:00:00) y fin (23:59:59)
        LocalDateTime fin = finMes.atTime(23, 59, 59);

        mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
    }

    /**
     * Genera el reporte basado en el rango de fechas seleccionado en los DatePicker.
     *
     * @param event El evento de acción.
     */
    @FXML
    void generarReportePersonalizado(ActionEvent event) {
        if (dpFechaInicio.getValue() != null && dpFechaFin.getValue() != null) {
            // Asegurar que el rango sea de inicio del primer día a fin del último día
            LocalDateTime inicio = dpFechaInicio.getValue().atStartOfDay();
            LocalDateTime fin = dpFechaFin.getValue().atTime(23, 59, 59);

            mostrarReporte(servicioReportes.obtenerVentas(inicio, fin));
        } else {
            mostrarAlerta("Error", "Debes seleccionar las fechas de inicio y fin.");
        }
    }

    /**
     * Maneja el evento de clic del botón "Regresar". Cierra la ventana actual.
     *
     * @param event El evento de acción.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }

    /**
     * Muestra un cuadro de diálogo de alerta al usuario.
     *
     * @param titulo El título de la alerta.
     * @param mensaje El mensaje a mostrar en el cuerpo de la alerta.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Abre una nueva ventana para mostrar el ranking de popularidad de platillos
     * (ControladorPopularidad).
     *
     * @param event El evento de acción.
     */
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