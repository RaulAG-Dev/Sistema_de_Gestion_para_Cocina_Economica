package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
// Se eliminan RepositorioJSON y ConvertidorPedido
import com.example.sistema.services.ServicioVentas;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controlador de la vista para el registro y gestión del Corte de Caja.
 *
 * <p>Esta clase se encarga de manejar la interfaz de usuario para ingresar los montos
 * iniciales/finales, visualizar las ventas del periodo y guardar el registro del corte.</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-01
 */
public class ControladorCorte implements Initializable {


    @FXML private Button regresarButton;
    @FXML private TableView<Pedido> ventasTable;
    @FXML private TableColumn<Pedido, Integer> numVentaColumn;
    @FXML private TableColumn<Pedido, String> detalleVentaColumn;
    @FXML private TableColumn<Pedido, Float> totalVentaColumn;
    @FXML private Label totalCorteLabel;
    @FXML private DatePicker fechaCortePicker;
    @FXML private Button guardarButton;
    @FXML private Button imprimirButton;
    @FXML private TextArea notasArea;

    private final ServicioVentas servicioVentas = ServicioVentas.getInstance();

    /**
     * Método de inicialización llamado automáticamente después de que se carga el archivo FXML.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numVentaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getId()));

        detalleVentaColumn.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(nombreSeguro(data.getValue()))
        );

        totalVentaColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getTotal()));

        fechaCortePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                java.util.Date fecha = java.sql.Date.valueOf(newDate);
                cargarVentasDelDia(fecha);
            }
        });

        java.util.Date hoy = new java.util.Date();
        fechaCortePicker.setValue(java.time.LocalDate.now());
        cargarVentasDelDia(hoy);
    }

    private void cargarVentasDelDia(Date fecha) {
        List<Pedido> ventas = servicioVentas.obtenerVentasPorFecha(fecha);
        ventasTable.getItems().setAll(ventas);

        float total = servicioVentas.obtenerTotalDelDia(fecha);
        totalCorteLabel.setText(String.format("Total: $%.2f", total));

        System.out.println("Ventas cargadas para el corte: " + ventas.size());
    }


    /**
     * Maneja el evento de clic del botón "Regresar", navegando de vuelta a la vista principal.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Stage stageActual = (Stage) scene.getWindow();
        stageActual.close();
    }

    /**
     * Maneja el evento de clic del botón "Guardar".
     */
    @FXML
    private void guardarCorte() {
        System.out.println("Guardando corte para la fecha: " + fechaCortePicker.getValue());
        // Aquí iría la llamada al servicio para guardar el objeto CorteCaja
    }

    /**
     * Maneja el evento de clic del botón "Imprimir".
     */
    @FXML
    private void imprimirCorte() {
        // CORRECCIÓN: Asegurar que la conversión de LocalDate a Date sea correcta y consistente
        java.util.Date fecha = java.sql.Date.valueOf(fechaCortePicker.getValue());
        List<Pedido> ventas = servicioVentas.obtenerVentasPorFecha(fecha);
        float total = servicioVentas.obtenerTotalDelDia(fecha);

        StringBuilder resumen = new StringBuilder();
        resumen.append("=== Corte de Caja ===\n");
        resumen.append("Fecha: ").append(fechaCortePicker.getValue()).append("\n"); // Usar LocalDate para mejor formato
        resumen.append("Ventas registradas: ").append(ventas.size()).append("\n\n");

        for (Pedido p : ventas) {
            String nombre = nombreSeguro(p);
            System.out.println("Pedido #" + p.getId() + " nombre: " + nombre + " - Total: $" + p.getTotal());
            resumen.append("Venta #").append(p.getId())
                    .append(" - ").append(nombre)
                    .append(" - Total: $").append(String.format("%.2f", p.getTotal())).append("\n");
        }

        resumen.append("\nTOTAL DEL DÍA: $").append(String.format("%.2f", total)).append("\n");
        resumen.append("======================\n");
        notasArea.setText(resumen.toString());
    }

    /**
     * Utilidad para generar un nombre de pedido legible y seguro contra valores nulos.
     */
    private String nombreSeguro(Pedido p) {
        if (p.getNombre() != null && !p.getNombre().isBlank()) return p.getNombre();
        if (p.getItems() == null || p.getItems().isEmpty()) return "Pedido vacío";

        // Reconstruye el resumen si el nombre principal es nulo, usando la lógica del pedido
        StringBuilder sb = new StringBuilder();
        for (var item : p.getItems()) {
            if (item.getPlatillo() != null && item.getPlatillo().getNombre() != null) {
                sb.append(item.getPlatillo().getNombre()).append(" x").append(item.getCantidad()).append(", ");
            }
        }
        return sb.length() == 0 ? "Pedido sin detalle" : sb.toString().replaceAll(", $", "");
    }
}