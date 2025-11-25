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

/**
 * **Controlador de la vista para la generación de reportes de popularidad y ventas de platillos.**
 * <p>
 * Permite a los usuarios consultar el ranking de platillos más vendidos
 * por un rango de fechas personalizado o predefinido (mensual), utilizando
 * los métodos de análisis del {@link ServicioVentas}.
 *
 * @author Michelle Chuc
 * @version 1.0
 * @since 2025-11-23
 */
public class ControladorPopularidad implements Initializable {

    // --- Componentes FXML de la Tabla de Resultados ---
    /** Tabla que muestra el ranking de platillos. */
    @FXML private TableView<Platillo> tablaPopularidad;
    /** Columna del nombre del platillo. */
    @FXML private TableColumn<Platillo, String> colPlatillo;
    /** Columna de la cantidad total vendida. */
    @FXML private TableColumn<Platillo, Integer> colCantidad;
    /** Columna de los ingresos totales generados. */
    @FXML private TableColumn<Platillo, Float> colIngresos;

    // --- Componentes FXML para Reporte Personalizado ---
    /** Selector de fecha de inicio para el rango personalizado. */
    @FXML private DatePicker dpFechaInicio;
    /** Selector de fecha de fin para el rango personalizado. */
    @FXML private DatePicker dpFechaFin;

    // --- Componentes FXML para Reporte Mensual ---
    /** Selector de mes. */
    @FXML private ComboBox<String> cmbMes;
    /** Selector de año para el reporte mensual. */
    @FXML private ComboBox<Integer> cmbAnioMensual;

    // --- Servicios ---
    /** Instancia del servicio de ventas para obtener datos y rankings. */
    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    /**
     * Inicializa el controlador:
     * 1. Carga los Combobox de meses y años.
     * 2. Muestra el ranking de platillos completo por defecto.
     *
     * @param url La ubicación utilizada para resolver rutas relativas.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     * @Override
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializar combo de meses (Nombres de Enero a Diciembre)
        String[] nombresMeses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        cmbMes.setItems(FXCollections.observableArrayList(nombresMeses));

        // Inicializar combo de años (últimos 5 años)
        int anioActual = Year.now().getValue();
        ObservableList<Integer> anios = FXCollections.observableArrayList();
        for (int i = anioActual; i >= anioActual - 4; i--) {
            anios.add(i);
        }
        cmbAnioMensual.setItems(anios);

        // Mostrar ranking completo al iniciar
        cargarRanking(servicioVentas.obtenerRankingPlatillos());
    }

    /**
     * Carga y actualiza los datos en la tabla de popularidad, configurando
     * las fábricas de celdas para mostrar el nombre, cantidad e ingresos.
     *
     * @param ranking La lista de objetos {@link Platillo} con estadísticas acumuladas.
     */
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

    /**
     * Genera y carga el ranking de platillos vendidos para el mes y año
     * seleccionado en los ComboBox.
     *
     * @param event El evento de acción que disparó la función.
     */
    @FXML
    void generarReporteMensualSeleccionado(ActionEvent event) {
        String mesSeleccionadoNombre = cmbMes.getSelectionModel().getSelectedItem();
        Integer anioSeleccionado = cmbAnioMensual.getSelectionModel().getSelectedItem();

        if (mesSeleccionadoNombre == null || anioSeleccionado == null) {
            return; // puedes mostrar alerta si quieres
        }

        int mesNumero = cmbMes.getSelectionModel().getSelectedIndex() + 1;
        // Calcular inicio (día 1 a las 00:00:00) y fin (último día a las 23:59:59) del mes
        LocalDate inicioMes = LocalDate.of(anioSeleccionado, mesNumero, 1);
        LocalDate finMes = inicioMes.with(TemporalAdjusters.lastDayOfMonth());

        LocalDateTime inicio = inicioMes.atStartOfDay();
        LocalDateTime fin = finMes.atTime(23, 59, 59);

        cargarRanking(servicioVentas.obtenerRankingPlatillosPorFechas(inicio, fin));
    }

    /**
     * Genera y carga el ranking de platillos vendidos para el rango de fechas
     * seleccionado en los DatePicker.
     * <p>
     * Si solo se selecciona la fecha de inicio, se usa esa fecha como rango (todo el día).
     *
     * @param event El evento de acción que disparó la función.
     */
    @FXML
    void generarReportePersonalizado(ActionEvent event) {
        if (dpFechaInicio.getValue() != null) {
            LocalDate inicioDia = dpFechaInicio.getValue();
            LocalDateTime inicio = inicioDia.atStartOfDay();
            // Inicializar fin como el final del día de inicio
            LocalDateTime fin = inicioDia.atTime(23, 59, 59);

            // Si también hay fecha final, la usamos como límite superior
            if (dpFechaFin.getValue() != null) {
                fin = dpFechaFin.getValue().atTime(23, 59, 59);
            }
            cargarRanking(servicioVentas.obtenerRankingPlatillosPorFechas(inicio, fin));
        }
        // No hacer nada si no se selecciona al menos la fecha de inicio
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
}
