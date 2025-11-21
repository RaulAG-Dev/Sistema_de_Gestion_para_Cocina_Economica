package com.example.sistema.controllers;

import com.example.sistema.models.Ingrediente;
import com.example.sistema.services.ServicioInventario;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.ResourceBundle;

/**
 * Controlador de la vista para la gestión y visualización del inventario de ingredientes.
 *
 * <p>Esta clase maneja la interfaz de usuario que muestra el stock de ingredientes,
 * permite la navegación al menú principal y podría mostrar alertas de stock mínimo.</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-01
 */
public class ControladorInventario implements Initializable {

    // --- Componentes FXML ---

    /**
     * Botón para regresar a la vista principal del sistema.
     */
    @FXML
    private Button regresarButton;
    /**
     * Tabla donde se visualizará la lista de ingredientes con su stock actual y mínimo.
     * <p>El tipo genérico {@code <?>} debe ser reemplazado por la clase Ingrediente.</p>
     */
    @FXML
    private TableView<Ingrediente> inventarioTable;
    /**
     * Área de texto utilizada para mostrar notificaciones o alertas de stock mínimo.
     */
    @FXML
    private TableColumn<Ingrediente, String> nombreColumn;
    @FXML
    private TableColumn<Ingrediente, Float> stockActualColumn;
    @FXML
    private TableColumn<Ingrediente, Float> stockMinimoColumn;
    @FXML
    private TableColumn<Ingrediente, String> unidadColumn;

    @FXML
    private TextArea notificacionesArea;

    @FXML private TextField nombreField;
    @FXML private TextField stockField;
    @FXML private TextField minimoField;
    @FXML private TextField unidadField;

    private final ServicioInventario servicioInventario = new ServicioInventario();

    /**
     * Método de inicialización llamado automáticamente después de que se carga el archivo FXML.
     * <p>Se utiliza para configurar la tabla de inventario y cargar los datos iniciales (stock de ingredientes).</p>
     *
     * @param url La ubicación relativa del objeto raíz.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar columnas
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        stockActualColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStockActual()));
        stockMinimoColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStockMinimo()));
        unidadColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnidades()));

        // Cargar inventario
        inventarioTable.getItems().setAll(servicioInventario.obtenerInventario());

        // Mostrar alertas
        mostrarAlertas();

        // Listener para selección
        inventarioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nombreField.setText(newSel.getNombre());
                stockField.setText(String.valueOf(newSel.getStockActual()));
                minimoField.setText(String.valueOf(newSel.getStockMinimo()));
                unidadField.setText(newSel.getUnidades());
            }
        });
    }

    private void mostrarAlertas() {
        var alertas = servicioInventario.obtenerAlertasStock();

        if (alertas.isEmpty()) {
            notificacionesArea.setText("Todos los ingredientes tienen stock suficiente.");
            return;
        }

        StringBuilder mensaje = new StringBuilder("Ingredientes con stock bajo:\n\n");
        for (Ingrediente i : alertas) {
            mensaje.append("- ").append(i.getNombre())
                    .append(": ").append(i.getStockActual())
                    .append(" ").append(i.getUnidades())
                    .append(" (mínimo requerido: ").append(i.getStockMinimo())
                    .append(")\n");
        }

        notificacionesArea.setText(mensaje.toString());
    }

    private void limpiarCampos() {
        nombreField.clear();
        stockField.clear();
        minimoField.clear();
        unidadField.clear();
    }


    /**
     * Maneja el evento de clic del botón "Regresar", navegando de vuelta a la vista principal.
     *
     * @param event El evento de acción (clic) que dispara la navegación.
     */
    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Scene scene = source.getScene();
        Stage stageActual = (Stage) scene.getWindow();
        stageActual.close();
    }

    @FXML
    void agregarIngrediente(ActionEvent event) {
        // Normalizar entradas
        String nombre = nombreField.getText().trim().toLowerCase();
        String stockTexto = stockField.getText().trim();
        String minimoTexto = minimoField.getText().trim();
        String unidad = unidadField.getText().trim().toLowerCase();

        // Validación de campos vacíos
        if (nombre.isEmpty() || stockTexto.isEmpty() || minimoTexto.isEmpty() || unidad.isEmpty()) {
            notificacionesArea.setText("Todos los campos son obligatorios para agregar.");
            return;
        }

        try {
            float stock = Float.parseFloat(stockTexto);
            float minimo = Float.parseFloat(minimoTexto);

            Ingrediente nuevo = new Ingrediente(0, nombre, stock, minimo, unidad);
            servicioInventario.agregarIngrediente(nuevo);

            inventarioTable.getItems().setAll(servicioInventario.obtenerInventario());
            mostrarAlertas();
            limpiarCampos();
            notificacionesArea.setText("Ingrediente agregado correctamente.");
        } catch (NumberFormatException e) {
            notificacionesArea.setText("Stock y mínimo deben ser números válidos.");
        } catch (Exception e) {
            notificacionesArea.setText("Error inesperado al agregar ingrediente.");
            e.printStackTrace();
        }
    }


    @FXML
    void guardarIngrediente(ActionEvent event) {
        Ingrediente seleccionado = inventarioTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            notificacionesArea.setText("Selecciona un ingrediente en la tabla para guardar cambios.");
            return;
        }

        try {
            seleccionado.setNombre(nombreField.getText().trim().toLowerCase());
            seleccionado.setStockActual(Float.parseFloat(stockField.getText().trim()));
            seleccionado.setStockMinimo(Float.parseFloat(minimoField.getText().trim()));
            seleccionado.setUnidades(unidadField.getText().trim().toLowerCase());

            servicioInventario.actualizarIngrediente(seleccionado);

            inventarioTable.getItems().setAll(servicioInventario.obtenerInventario());
            mostrarAlertas();
            limpiarCampos();
            notificacionesArea.setText("Cambios guardados correctamente.");
        } catch (NumberFormatException e) {
            notificacionesArea.setText("Stock y mínimo deben ser números válidos.");
        } catch (Exception e) {
            notificacionesArea.setText("Error inesperado al guardar cambios.");
            e.printStackTrace();
        }
    }

    @FXML
    void eliminarIngrediente(ActionEvent event) {
        Ingrediente seleccionado = inventarioTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            notificacionesArea.setText("Selecciona un ingrediente para eliminar.");
            return;
        }

        boolean eliminado = servicioInventario.eliminarIngrediente(seleccionado.getId());
        if (eliminado) {
            inventarioTable.getItems().setAll(servicioInventario.obtenerInventario());
            mostrarAlertas();
            limpiarCampos();
            notificacionesArea.setText("Ingrediente eliminado.");
        } else {
            notificacionesArea.setText("No se pudo eliminar el ingrediente.");
        }
    }

}