package com.example.sistema.controllers;

import com.example.sistema.models.IngredientePlatillo;
import com.example.sistema.models.Platillo;
import com.example.sistema.services.ServicioMenu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Controlador para el modal de creación y edición de platillos.
 *
 * <p>Esta clase gestiona la interfaz del formulario modal, permitiendo a los usuarios
 * ingresar o modificar los detalles de un platillo y sus ingredientes.</p>
 *
 * @author Raul Aguayo
 * @version 1.1
 * @since 2025-11-02
 */
public class ControladorPlatilloModal {


    @FXML private Label tituloLabel;
    @FXML private TextField nombreField;
    @FXML private TextField descripcionField;
    @FXML private TextField precioField;
    @FXML private ComboBox<String> tipoMenuComboBox;
    @FXML private TextField txtIngredienteNombre;
    @FXML private TextField txtIngredienteCantidad;
    @FXML private TableView<IngredientePlatillo> tablaIngredientes;
    @FXML private TableColumn<IngredientePlatillo, String> colIngredienteNombre;
    @FXML private TableColumn<IngredientePlatillo, Float> colIngredienteCantidad;

    // --- Dependencias y Estado ---
    private Platillo platilloActual;
    private ServicioMenu servicioMenu;
    private ControladorPrincipal controladorPrincipal;

    // Lista observable para que la tabla funcione visualmente
    private ObservableList<IngredientePlatillo> listaIngredientes = FXCollections.observableArrayList();

    /**
     * Método de inicialización llamado manualmente por el controlador padre.
     */
    public void inicializar(Platillo platillo, ServicioMenu servicio, ControladorPrincipal principal) {
        this.platilloActual = platillo;
        this.servicioMenu = servicio;
        this.controladorPrincipal = principal;

        // 1. Carga las opciones del ComboBox
        tipoMenuComboBox.setItems(FXCollections.observableArrayList("Desayunos", "Almuerzos", "Cenas"));

        // 2. Configuración de la Tabla de Ingredientes (Parte de la VISTA)
        // Vinculamos las columnas con los atributos del modelo IngredientePlatillo
        colIngredienteNombre.setCellValueFactory(new PropertyValueFactory<>("nombreIngrediente"));
        colIngredienteCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadNecesaria"));

        // Asignamos la lista vacía a la tabla
        tablaIngredientes.setItems(listaIngredientes);

        // 3. Cargar datos si es edición
        if (platillo != null) {
            // Modo Edición
            tituloLabel.setText("Editar Platillo: " + platillo.getNombre());
            nombreField.setText(platillo.getNombre());
            descripcionField.setText(platillo.getDescripcion());
            precioField.setText(String.valueOf(platillo.getPrecio()));
            tipoMenuComboBox.setValue(platillo.getTipoMenu());

            // falta agregar logica para cargar ingredientes en la tabla de ListaIngredientes
        } else {
            // Modo Creación
            tituloLabel.setText("Añadir Nuevo Platillo");
            tipoMenuComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Método vinculado al botón "+" de la vista.
     * Agrega un ingrediente a la lista temporal de la receta.
     */
    @FXML
    private void agregarIngrediente() {
        // TODO: LÓGICA PENDIENTE (A cargo del Backend/Compañero)
        // 1. Obtener texto de txtIngredienteNombre y txtIngredienteCantidad.
        // 2. Validar que no estén vacíos y que cantidad sea número.
        // 3. Crear instancia de IngredientePlatillo.
        // 4. Agregar a listaIngredientes.add(...);
        // 5. Limpiar los campos de texto.
    }

    /**
     * Maneja el evento de clic del botón "Guardar".
     */
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
                // Creación
                platilloActual = new Platillo(0, nombre, descripcion, precio, true, null, tipoMenu);
            } else {
                // Actualización
                platilloActual.setNombre(nombre);
                platilloActual.setDescripcion(descripcion);
                platilloActual.setPrecio(precio);
                platilloActual.setTipoMenu(tipoMenu);
            }

            // TODO: Tu compañero deberá modificar esto para guardar también la 'listaIngredientes' junto con el platillo.

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