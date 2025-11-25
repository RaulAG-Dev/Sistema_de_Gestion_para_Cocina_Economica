package com.example.sistema.controllers;

import com.example.sistema.models.Ingrediente;
import com.example.sistema.services.ServicioInventario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;

/**
 * **Controlador de la vista para la gestión del inventario de ingredientes (CRUD).**
 * <p>
 * Permite a los usuarios visualizar, añadir, modificar y eliminar ingredientes.
 * También muestra alertas sobre ingredientes con stock bajo, utilizando
 * {@link ServicioInventario}.
 *
 * @author Raul Aguayo y Michelle Chuc
 * @version 3.0
 * @since 2025-11-21
 */
public class ControladorInventario implements Initializable {

    // --- Componentes FXML de la Vista ---
    @FXML private Button regresarButton;
    /** Tabla principal que muestra la lista de todos los ingredientes. */
    @FXML private TableView<Ingrediente> inventarioTable;
    @FXML private TableColumn<Ingrediente, String> nombreColumn;
    @FXML private TableColumn<Ingrediente, Float> stockActualColumn;
    @FXML private TableColumn<Ingrediente, Float> stockMinimoColumn;
    @FXML private TableColumn<Ingrediente, String> unidadColumn;
    /** Área de texto para mostrar notificaciones, alertas y errores de validación. */
    @FXML private TextArea notificacionesArea;

    // Campos del formulario
    @FXML private TextField nombreField;
    @FXML private TextField stockField;
    @FXML private TextField minimoField;
    @FXML private TextField unidadField;

    // --- Servicios ---
    /** Instancia del servicio de inventario para interactuar con los datos. */
    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();

    /**
     * Inicializa el controlador, configura las fábricas de celdas y carga los datos
     * de inventario y las alertas al inicio.
     *
     * @param url La ubicación utilizada para resolver rutas relativas.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     * @Override
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configuración de Cell Value Factories
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        stockActualColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStockActual()));
        stockMinimoColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStockMinimo()));
        unidadColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnidades()));
        cargarDatosYAlertas();
        // Listener para cargar los datos del ingrediente seleccionado en el formulario
        inventarioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nombreField.setText(newSel.getNombre());
                stockField.setText(String.valueOf(newSel.getStockActual()));
                minimoField.setText(String.valueOf(newSel.getStockMinimo()));
                unidadField.setText(newSel.getUnidades());
            }
        });
    }

    /**
     * Carga todos los ingredientes en la tabla y actualiza el área de notificaciones
     * con las alertas de stock.
     */
    private void cargarDatosYAlertas() {
        inventarioTable.getItems().setAll(servicioInventario.obtenerInventario());
        mostrarAlertas();
    }

    /**
     * Consulta el servicio de inventario para obtener la lista de ingredientes en alerta
     * y muestra un mensaje detallado en el área de notificaciones.
     */
    private void mostrarAlertas() {
        List<Ingrediente> alertas = servicioInventario.obtenerAlertasStock();

        if (alertas.isEmpty()) {
            notificacionesArea.setText("Todos los ingredientes tienen stock suficiente.");
            return;
        }

        StringBuilder mensaje = new StringBuilder("--- ALERTA DE STOCK BAJO ---\n\n");
        for (Ingrediente i : alertas) {
            mensaje.append("- ").append(i.getNombre())
                    .append(": ").append(String.format("%.2f", i.getStockActual()))
                    .append(" ").append(i.getUnidades())
                    .append(" (Mínimo: ").append(String.format("%.2f", i.getStockMinimo()))
                    .append(")\n");
        }

        notificacionesArea.setText(mensaje.toString());
    }

    /**
     * Limpia todos los campos de texto del formulario de edición/creación.
     */
    private void limpiarCampos() {
        nombreField.clear();
        stockField.clear();
        minimoField.clear();
        unidadField.clear();
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
     * Maneja la creación de un nuevo ingrediente.
     * <p>
     * Realiza la validación de los datos (no vacíos, números positivos) antes de
     * llamar a {@link ServicioInventario#agregarIngrediente(Ingrediente)}.
     *
     * @param event El evento de acción.
     */
    @FXML
    void agregarIngrediente(ActionEvent event) {
        String nombre = nombreField.getText().trim();
        try {
            float stock = Float.parseFloat(stockField.getText().trim());
            float minimo = Float.parseFloat(minimoField.getText().trim());
            String unidad = unidadField.getText().trim();

            if (nombre.isEmpty() || unidad.isEmpty()) throw new IllegalArgumentException("Campos vacíos.");
            if (stock < 0 || minimo < 0) throw new IllegalArgumentException("Stock y mínimo deben ser positivos.");


            Ingrediente nuevo = new Ingrediente(0, nombre, stock, minimo, unidad);
            servicioInventario.agregarIngrediente(nuevo);

            cargarDatosYAlertas();
            limpiarCampos();
            notificacionesArea.setText("Ingrediente agregado correctamente.");
        } catch (NumberFormatException e) {
            notificacionesArea.setText("Stock y mínimo deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            notificacionesArea.setText("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            notificacionesArea.setText("Error inesperado al agregar ingrediente.");
        }
    }


    /**
     * Maneja la actualización de un ingrediente seleccionado.
     * <p>
     * Asigna los nuevos valores desde el formulario al objeto seleccionado y
     * llama a {@link ServicioInventario#actualizarIngrediente(Ingrediente)}.
     *
     * @param event El evento de acción.
     */
    @FXML
    void guardarIngrediente(ActionEvent event) {
        Ingrediente seleccionado = inventarioTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            notificacionesArea.setText("Selecciona un ingrediente en la tabla para guardar cambios.");
            return;
        }

        try {
            // Asignar nuevos valores, validando la conversión a float
            seleccionado.setNombre(nombreField.getText().trim());
            seleccionado.setStockActual(Float.parseFloat(stockField.getText().trim()));
            seleccionado.setStockMinimo(Float.parseFloat(minimoField.getText().trim()));
            seleccionado.setUnidades(unidadField.getText().trim());

            servicioInventario.actualizarIngrediente(seleccionado); // Actualiza en memoria y persiste

            cargarDatosYAlertas();
            limpiarCampos();
            notificacionesArea.setText("Cambios guardados correctamente.");
        } catch (NumberFormatException e) {
            notificacionesArea.setText("Stock y mínimo deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            notificacionesArea.setText("Error de validación: " + e.getMessage());
        } catch (Exception e) {
            notificacionesArea.setText("Error inesperado al guardar cambios.");
        }
    }

    /**
     * Maneja la eliminación de un ingrediente seleccionado.
     * <p>
     * Llama a {@link ServicioInventario#eliminarIngrediente(int)} y actualiza la vista.
     *
     * @param event El evento de acción.
     */
    @FXML
    void eliminarIngrediente(ActionEvent event) {
        Ingrediente seleccionado = inventarioTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            notificacionesArea.setText("Selecciona un ingrediente para eliminar.");
            return;
        }

        boolean eliminado = servicioInventario.eliminarIngrediente(seleccionado.getId());
        if (eliminado) {
            cargarDatosYAlertas();
            limpiarCampos();
            notificacionesArea.setText("Ingrediente eliminado.");
        } else {
            notificacionesArea.setText("No se pudo eliminar el ingrediente.");
        }
    }

}