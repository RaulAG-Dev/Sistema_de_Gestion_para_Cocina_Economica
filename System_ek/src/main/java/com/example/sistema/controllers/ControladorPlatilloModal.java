/**
 * package com.example.sistema.controllers;
 *
 * Controlador para la ventana modal (diálogo) de gestión de platillos (añadir/editar).
 * Permite al usuario ingresar los detalles de un platillo, su precio, tipo de menú
 * y componer la receta seleccionando ingredientes del inventario.
 *
 * @author Emiliano Ávila
 * @version 1.0
 * @since 2025-11-24
 */
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

    // --- Componentes FXML ---
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

    // --- Variables de Estado y Dependencias ---
    private Platillo platilloActual;
    private ServicioMenu servicioMenu;
    private ControladorPrincipal controladorPrincipal;

    private final ObservableList<ItemReceta> recetaObservableList = FXCollections.observableArrayList();
    private final ServicioInventario servicioInventario = ServicioInventario.getInstance();

    /**
     * Inicializa el controlador después de que todos los elementos FXML han sido procesados.
     * Configura los ComboBoxes, las columnas de la tabla y los listeners necesarios.
     *
     * @param url La ubicación utilizada para resolver rutas relativas para el objeto raíz, o null si no se conoce.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz, o null si no se conoce.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización de ComboBoxes
        tipoMenuComboBox.setItems(FXCollections.observableArrayList("Desayunos", "Almuerzos", "Cenas"));
        List<Ingrediente> ingredientesDisponibles = servicioInventario.obtenerInventario();
        ingredienteComboBox.setItems(FXCollections.observableArrayList(ingredientesDisponibles));

        // Configuración de las Columnas de la Tabla
        colIngredienteNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIngrediente().getNombre()));
        colIngredienteCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadRequerida"));
        colIngredienteUnidad.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIngrediente().getUnidades()));
        tablaIngredientes.setItems(recetaObservableList);

        // Conversor para mostrar el nombre del Ingrediente en el ComboBox
        ingredienteComboBox.setConverter(new StringConverter<Ingrediente>() {
            @Override public String toString(Ingrediente object) { return object != null ? object.getNombre() : ""; }
            @Override public Ingrediente fromString(String string) { return null; }
        });

        // Listener para actualizar la etiqueta de unidad al seleccionar un ingrediente
        ingredienteComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldIng, newIng) -> {
            unidadLabel.setText(newIng != null ? newIng.getUnidades() : "Unid.");
        });
    }

    /**
     * Método para inicializar el modal con los datos necesarios y configurar el modo (Añadir o Editar).
     * Este método se llama externamente al cargar el FXML.
     *
     * @param platillo El objeto Platillo a editar, o null si se está creando un platillo nuevo.
     * @param servicio El ServicioMenu utilizado para interactuar con la lógica de negocio.
     * @param principal El ControladorPrincipal para poder invocar la actualización de la vista principal.
     */
    public void inicializar(Platillo platillo, ServicioMenu servicio, ControladorPrincipal principal) {
        this.platilloActual = platillo;
        this.servicioMenu = servicio;
        this.controladorPrincipal = principal;

        if (platillo != null) {
            // Modo Edición
            tituloLabel.setText("Editar Platillo: " + platillo.getNombre());
            nombreField.setText(platillo.getNombre());
            descripcionField.setText(platillo.getDescripcion());
            precioField.setText(String.valueOf(platillo.getPrecio()));
            tipoMenuComboBox.setValue(platillo.getTipoMenu());

            if (platillo.getReceta() != null) {
                recetaObservableList.setAll(platillo.getReceta());
            }

        } else {
            // Modo Nuevo Platillo
            tituloLabel.setText("Añadir Nuevo Platillo");
            tipoMenuComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Maneja la acción de agregar un ingrediente a la receta del platillo.
     * Valida que se haya seleccionado un ingrediente y una cantidad válida.
     * Si el ingrediente ya existe en la receta, se reemplaza con la nueva cantidad.
     *
     * @param actionEvent El evento de la acción (por ejemplo, clic en el botón).
     */
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

            // Crea el nuevo item y lo añade/reemplaza en la lista observable
            ItemReceta newItem = new ItemReceta(ingredienteSeleccionado, cantidad);
            recetaObservableList.removeIf(item -> item.getIngrediente().getId() == ingredienteSeleccionado.getId());
            recetaObservableList.add(newItem);

            // Limpia los campos de entrada
            cantidadField.clear();
            ingredienteComboBox.getSelectionModel().clearSelection();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "La cantidad debe ser un número válido.").showAndWait();
        }
    }

    /**
     * Intenta guardar o actualizar el platillo en el sistema.
     * Realiza validaciones básicas de campos y notifica al usuario en caso de error.
     * Si tiene éxito, llama al servicio para guardar y cierra el modal.
     */
    @FXML
    private void guardar() {
        try {
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            float precio = Float.parseFloat(precioField.getText());
            String tipoMenu = tipoMenuComboBox.getValue();

            // Validación de campos obligatorios
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
                // Creación de nuevo platillo
                platilloActual = new Platillo(0, nombre, descripcion, precio, true, recetaFinal, tipoMenu);
            } else {
                // Actualización de platillo existente
                platilloActual.setNombre(nombre);
                platilloActual.setDescripcion(descripcion);
                platilloActual.setPrecio(precio);
                platilloActual.setTipoMenu(tipoMenu);
                platilloActual.setReceta(recetaFinal);
            }

            servicioMenu.guardarPlatillo(platilloActual);

            // Actualiza la vista principal y cierra el modal
            controladorPrincipal.refrescarVista();
            cancelar();

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El precio debe ser un número válido.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Cierra la ventana modal actual.
     */
    @FXML
    private void cancelar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}