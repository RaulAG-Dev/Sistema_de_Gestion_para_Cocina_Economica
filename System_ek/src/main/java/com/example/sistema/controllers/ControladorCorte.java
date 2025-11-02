package com.example.sistema.controllers;

import com.example.sistema.models.Pedido;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    // --- Componentes FXML ---

    /**
     * Botón para regresar a la vista principal del sistema.
     */
    @FXML private Button regresarButton;
    /**
     * Tabla donde se visualizarán los pedidos (ventas) realizadas durante el periodo del corte.
     */
    @FXML private TableView<Pedido> ventasTable;
    /**
     * Columna de la tabla para mostrar el número o ID de la venta/pedido.
     */
    @FXML private TableColumn<Pedido, Integer> numVentaColumn;
    /**
     * Columna de la tabla para mostrar un detalle breve del contenido de la venta/pedido.
     */
    @FXML private TableColumn<Pedido, String> detalleVentaColumn;
    /**
     * Columna de la tabla para mostrar el monto total de la venta/pedido.
     */
    @FXML private TableColumn<Pedido, Float> totalVentaColumn;
    /**
     * Etiqueta (Label) que muestra la suma total de las ventas esperadas para el corte.
     */
    @FXML private Label totalCorteLabel;
    /**
     * Selector de fecha para indicar la fecha en que se realiza el corte.
     */
    @FXML private DatePicker fechaCortePicker;
    /**
     * Botón para guardar el registro final del corte de caja en la persistencia.
     */
    @FXML private Button guardarButton;
    /**
     * Botón para imprimir el resumen o ticket del corte de caja.
     */
    @FXML private Button imprimirButton;
    /**
     * Área de texto para añadir notas u observaciones relevantes sobre el corte.
     */
    @FXML private TextArea notasArea;


    /**
     * Método de inicialización llamado automáticamente después de que se carga el archivo FXML.
     * <p>Se utiliza para configurar la tabla, cargar datos iniciales o listeners.</p>
     *
     * @param url La ubicación relativa del objeto raíz.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Lógica de inicialización: configuración de columnas, carga de datos.
    }

    /**
     * Maneja el evento de clic del botón "Regresar", navegando de vuelta a la vista principal.
     *
     * @param event El evento de acción (clic) que dispara la navegación.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/sistema/principal-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) regresarButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gestión Cocina Económica - Principal");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error al cargar la vista principal.");
            e.printStackTrace();
        }
    }

    /**
     * Maneja el evento de clic del botón "Guardar".
     * <p>Lógica principal para validar los montos y registrar el corte final de caja
     * en el sistema de persistencia (base de datos).</p>
     */
    @FXML
    private void guardarCorte() {
        System.out.println("Guardando corte para la fecha: " + fechaCortePicker.getValue());
        // Aquí iría la llamada al servicio para guardar el objeto CorteCaja
    }

    /**
     * Maneja el evento de clic del botón "Imprimir".
     * <p>Lógica para generar el formato de impresión del resumen del corte.</p>
     */
    @FXML
    private void imprimirCorte() {
        System.out.println("Imprimiendo resumen del corte.");
        // Aquí iría la lógica de impresión
    }
}