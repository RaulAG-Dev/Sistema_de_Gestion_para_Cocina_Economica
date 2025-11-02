package com.example.sistema.controllers;

import com.example.sistema.models.Platillo;
import com.example.sistema.services.ServicioMenu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ControladorPlatilloModal {

    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField precioField;
    @FXML private ComboBox<String> tipoMenuComboBox;

    private Platillo platilloActual;
    private ServicioMenu servicioMenu;
    private ControladorPrincipal controladorPrincipal;

    public void inicializar(Platillo platillo, ServicioMenu servicio, ControladorPrincipal principal) {
        this.platilloActual = platillo;
        this.servicioMenu = servicio;
        this.controladorPrincipal = principal;

        tipoMenuComboBox.setItems(FXCollections.observableArrayList("Desayunos", "Almuerzos", "Cenas"));

        if (platillo != null) {
            tituloLabel.setText("Editar Platillo: " + platillo.getNombre());
            nombreField.setText(platillo.getNombre());
            descripcionField.setText(platillo.getDescripcion());
            precioField.setText(String.valueOf(platillo.getPrecio()));
            tipoMenuComboBox.setValue(platillo.getTipoMenu());
        } else {
            tituloLabel.setText("Añadir Nuevo Platillo");
            tipoMenuComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void guardar() {
        try {
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            float precio = Float.parseFloat(precioField.getText());
            String tipoMenu = tipoMenuComboBox.getValue();

            if (nombre.isEmpty() || tipoMenu == null) {
                new Alert(Alert.AlertType.ERROR, "El nombre y el tipo de menú son obligatorios.").showAndWait();
                return;
            }

            if (platilloActual == null) {
                platilloActual = new Platillo(0, nombre, descripcion, precio, true, null, tipoMenu);
            } else {
                platilloActual.setNombre(nombre);
                platilloActual.setDescripcion(descripcion);
                platilloActual.setPrecio(precio);
                platilloActual.setTipoMenu(tipoMenu);
            }

            servicioMenu.guardarPlatillo(platilloActual);

            controladorPrincipal.refrescarVista();
            cancelar();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El precio debe ser un número válido.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}