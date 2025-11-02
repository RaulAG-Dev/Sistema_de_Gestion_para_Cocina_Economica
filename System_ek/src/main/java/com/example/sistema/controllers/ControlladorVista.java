package com.example.sistema.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

/**
 * Controlador genérico para una vista simple que muestra información en un formato de cuadrícula.
 *
 * <p>Esta clase se utiliza para gestionar una interfaz de usuario básica que presenta datos
 * en un {@code GridPane} y ofrece un campo de texto para detalles o notas, además de un botón de navegación.
 * Podría ser reutilizada para vistas de inventario o listados.</p>
 *
 * @author Edwin
 * @version 2.0
 * @since 2025-11-01
 */
public class ControlladorVista {
    // --- Componentes FXML ---

    /**
     * Botón utilizado para regresar a la vista anterior o al menú principal.
     */
    @FXML
    private Button regresarButton;

    /**
     * Etiqueta (Label) utilizada como título o indicador de la sección actual (ej. "Inventario Actual").
     */
    @FXML
    private Label inventarioLabel;

    /**
     * Contenedor de cuadrícula (GridPane) donde se listarán y visualizarán los elementos de la vista
     * (ej. tarjetas de productos o filas de inventario).
     */
    @FXML
    private GridPane inventarioGrid;

    /**
     * Área de texto utilizada para mostrar detalles extendidos del ítem seleccionado o notas generales.
     */
    @FXML
    private TextArea detalleTextArea;
}//Fin de la Clase
