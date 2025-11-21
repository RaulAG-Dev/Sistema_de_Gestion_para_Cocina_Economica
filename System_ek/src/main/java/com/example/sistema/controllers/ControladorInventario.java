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

public class ControladorInventario implements Initializable {


    @FXML private Button regresarButton;
    @FXML private TableView<Ingrediente> inventarioTable;
    @FXML private TableColumn<Ingrediente, String> nombreColumn;
    @FXML private TableColumn<Ingrediente, Float> stockActualColumn;
    @FXML private TableColumn<Ingrediente, Float> stockMinimoColumn;
    @FXML private TableColumn<Ingrediente, String> unidadColumn;
    @FXML private TextArea notificacionesArea;
    @FXML private TextField nombreField;
    @FXML private TextField stockField;
    @FXML private TextField minimoField;
    @FXML private TextField unidadField;

    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nombreColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        stockActualColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStockActual()));
        stockMinimoColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getStockMinimo()));
        unidadColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUnidades()));
        cargarDatosYAlertas();
        inventarioTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                nombreField.setText(newSel.getNombre());
                stockField.setText(String.valueOf(newSel.getStockActual()));
                minimoField.setText(String.valueOf(newSel.getStockMinimo()));
                unidadField.setText(newSel.getUnidades());
            }
        });
    }

    private void cargarDatosYAlertas() {
        inventarioTable.getItems().setAll(servicioInventario.obtenerInventario());
        mostrarAlertas();
    }

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

    private void limpiarCampos() {
        nombreField.clear();
        stockField.clear();
        minimoField.clear();
        unidadField.clear();
    }


    @FXML
    void manejarRegreso(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stageActual = (Stage) source.getScene().getWindow();
        stageActual.close();
    }

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


    @FXML
    void guardarIngrediente(ActionEvent event) {
        Ingrediente seleccionado = inventarioTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            notificacionesArea.setText("Selecciona un ingrediente en la tabla para guardar cambios.");
            return;
        }

        try {
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