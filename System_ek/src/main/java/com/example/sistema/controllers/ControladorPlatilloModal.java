package com.example.sistema.controllers;

import com.example.sistema.models.Ingrediente;
import com.example.sistema.models.ItemReceta;
import com.example.sistema.models.Platillo;
import com.example.sistema.services.ServicioInventario;
import com.example.sistema.services.ServicioMenu;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ControladorPlatilloModal implements Initializable {

    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField precioField;
    @FXML private ComboBox<String> tipoMenuComboBox;

    @FXML private ComboBox<Ingrediente> ingredienteComboBox;
    @FXML private TextField cantidadField;
    @FXML private Label unidadLabel;
    @FXML private TableView<ItemReceta> tablaIngredientes;
    @FXML private TableColumn<ItemReceta, String> colIngredienteNombre;
    @FXML private TableColumn<ItemReceta, Float> colIngredienteCantidad;
    @FXML private TableColumn<ItemReceta, String> colIngredienteUnidad;

    private Platillo platilloActual;
    private ServicioMenu servicioMenu;
    private ControladorPrincipal controladorPrincipal;

    private final ObservableList<ItemReceta> recetaObservableList = FXCollections.observableArrayList();
    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoMenuComboBox.setItems(FXCollections.observableArrayList("Desayunos", "Almuerzos", "Cenas"));
        List<Ingrediente> ingredientesDisponibles = servicioInventario.obtenerInventario();
        ingredienteComboBox.setItems(FXCollections.observableArrayList(ingredientesDisponibles));

        colIngredienteNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIngrediente().getNombre()));
        colIngredienteCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadRequerida"));
        colIngredienteUnidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIngrediente().getUnidades()));
        tablaIngredientes.setItems(recetaObservableList);

        ingredienteComboBox.setConverter(new StringConverter<Ingrediente>() {
            @Override public String toString(Ingrediente object) { return object != null ? object.getNombre() : ""; }
            @Override public Ingrediente fromString(String string) { return null; }
        });

        ingredienteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldIng, newIng) -> {
            unidadLabel.setText(newIng != null ? newIng.getUnidades() : "Unid.");
        });
    }

    public void inicializar(Platillo platillo, ServicioMenu servicio, ControladorPrincipal principal) {
        this.platilloActual = platillo;
        this.servicioMenu = servicio;
        this.controladorPrincipal = principal;

        if (platillo != null) {
            tituloLabel.setText("Editar Platillo: " + platillo.getNombre());
            nombreField.setText(platillo.getNombre());
            descripcionField.setText(platillo.getDescripcion());
            precioField.setText(String.valueOf(platillo.getPrecio()));
            tipoMenuComboBox.setValue(platillo.getTipoMenu());

            if (platillo.getReceta() != null) {
                recetaObservableList.setAll(platillo.getReceta());
            }

        } else {
            tituloLabel.setText("Añadir Nuevo Platillo");
            tipoMenuComboBox.getSelectionModel().selectFirst();
        }
    }

    @FXML
    public void agregarIngrediente(ActionEvent actionEvent) {
        Ingrediente ingredienteSeleccionado = ingredienteComboBox.getSelectionModel().getSelectedItem();
        String cantidadTexto = cantidadField.getText();

        if (ingredienteSeleccionado == null || cantidadTexto.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Debes seleccionar un ingrediente y especificar la cantidad.").showAndWait();
            return;
        }

        try {
            float cantidad = Float.parseFloat(cantidadTexto);
            if (cantidad <= 0) {
                new Alert(Alert.AlertType.ERROR, "La cantidad debe ser un número positivo.").showAndWait();
                return;
            }

            ItemReceta newItem = new ItemReceta(ingredienteSeleccionado, cantidad);
            recetaObservableList.removeIf(item -> item.getIngrediente().getId() == ingredienteSeleccionado.getId());
            recetaObservableList.add(newItem);

            cantidadField.clear();
            ingredienteComboBox.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "La cantidad debe ser un número válido.").showAndWait();
        }
    }

    @FXML
    private void guardar() {
        try {
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            float precio = Float.parseFloat(precioField.getText());
            String tipoMenu = tipoMenuComboBox.getValue();

            if (nombre.isEmpty() || tipoMenu == null || recetaObservableList.isEmpty()) {
                String mensaje = "Faltan campos obligatorios: ";
                if (nombre.isEmpty()) mensaje += "Nombre, ";
                if (tipoMenu == null) mensaje += "Tipo de Menú, ";
                if (recetaObservableList.isEmpty()) mensaje += "Receta. ";
                new Alert(Alert.AlertType.ERROR, mensaje.replaceAll(", $", "")).showAndWait();
                return;
            }

            List<ItemReceta> recetaFinal = new ArrayList<>(recetaObservableList);
            boolean esNuevo = (platilloActual == null);

            if (esNuevo) {
                platilloActual = new Platillo(0, nombre, descripcion, precio, true, recetaFinal, tipoMenu);
            } else {
                platilloActual.setNombre(nombre);
                platilloActual.setDescripcion(descripcion);
                platilloActual.setPrecio(precio);
                platilloActual.setTipoMenu(tipoMenu);
                platilloActual.setReceta(recetaFinal);
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