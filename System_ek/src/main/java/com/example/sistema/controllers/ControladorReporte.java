package com.example.sistema.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Controlador de la vista de generación y gestión de reportes.
 *
 * <p>Esta clase se encarga de manejar la interfaz de usuario para seleccionar un rango
 * de fechas, generar reportes de datos (ventas, inventario, etc.) y gestionar las
 * opciones de guardado, borrado e impresión de dicho informe.</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-02
 */
public class ControladorReporte {
    // --- Componentes FXML ---

    /**
     * Botón para guardar los cambios o configuración de un reporte.
     */
    @FXML
    private Button guardarCambiosButton;

    /**
     * Botón principal utilizado para iniciar el proceso de generación del reporte.
     */
    @FXML
    private Button reporteButton;

    /**
     * Botón para regresar a la vista anterior o principal.
     */
    @FXML
    private Button regresarButton;

    /**
     * Botón utilizado para eliminar o borrar un reporte existente o datos seleccionados.
     */
    @FXML
    private Button borrarButton;

    /**
     * Botón para enviar el reporte generado a la impresora.
     */
    @FXML
    private Button imprimirButton;

    /**
     * Selector de fecha para definir la fecha de inicio o fin del reporte.
     */
    @FXML
    private DatePicker fechaPicker;

    /**
     * Etiqueta (Label) que puede mostrar el título del reporte o mensajes de estado.
     */
    @FXML
    private Label reporteLabel;

    /**
     * Área de texto donde se visualizará el contenido detallado del reporte generado.
     */
    @FXML
    private TextArea reporteTextArea;
}//Fin de la Clase