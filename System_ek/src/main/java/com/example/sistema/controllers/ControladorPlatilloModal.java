package com.example.sistema.controllers;

import com.example.sistema.models.Platillo;
import com.example.sistema.services.ServicioMenu;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador para el modal de creación y edición de platillos.
 *
 * <p>Esta clase gestiona la interfaz del formulario modal, permitiendo a los usuarios
 * ingresar o modificar los detalles de un platillo (nombre, descripción, precio, tipo)
 * y persistir esos cambios a través del {@code ServicioMenu}.</p>
 *
 * @author Raul Aguayo
 * @version 1.0
 * @since 2025-11-02
 */
public class ControladorPlatilloModal {

    // --- Componentes FXML ---

    /**
     * Etiqueta (Label) que muestra el título del modal ("Añadir" o "Editar Platillo").
     */
    @FXML private Label tituloLabel;
    /**
     * Campo de texto para ingresar o modificar el nombre del platillo.
     */
    @FXML private TextField nombreField;
    /**
     * Campo de texto para ingresar o modificar la descripción del platillo.
     */
    @FXML private TextField descripcionField;
    /**
     * Campo de texto para ingresar o modificar el precio del platillo.
     */
    @FXML private TextField precioField;
    /**
     * ComboBox para seleccionar el tipo de menú al que pertenece el platillo.
     */
    @FXML private ComboBox<String> tipoMenuComboBox;

    // --- Dependencias y Estado ---

    /**
     * La instancia del platillo que se está editando o creando. Es {@code null} si es nuevo.
     */
    private Platillo platilloActual;
    /**
     * Instancia del servicio que maneja la lógica de negocio para la gestión del menú.
     */
    private ServicioMenu servicioMenu;
    /**
     * Referencia al controlador principal para poder actualizar la vista tras guardar un cambio.
     */
    private ControladorPrincipal controladorPrincipal;

    /**
     * Método de inicialización llamado manualmente por el controlador padre.
     * <p>Configura las dependencias, inicializa el ComboBox y carga los datos si se está editando un platillo existente.</p>
     *
     * @param platillo El objeto Platillo a editar, o {@code null} si es una creación nueva.
     * @param servicio La instancia de {@code ServicioMenu} para la persistencia.
     * @param principal La instancia del controlador principal para refrescar datos.
     */
    public void inicializar(Platillo platillo, ServicioMenu servicio, ControladorPrincipal principal) {
        this.platilloActual = platillo;
        this.servicioMenu = servicio;
        this.controladorPrincipal = principal;

        // Carga las opciones del ComboBox
        tipoMenuComboBox.setItems(FXCollections.observableArrayList("Desayunos", "Almuerzos", "Cenas"));

        if (platillo != null) {
            // Modo Edición
            tituloLabel.setText("Editar Platillo: " + platillo.getNombre());
            nombreField.setText(platillo.getNombre());
            descripcionField.setText(platillo.getDescripcion());
            precioField.setText(String.valueOf(platillo.getPrecio()));
            tipoMenuComboBox.setValue(platillo.getTipoMenu()); // Asumiendo que Platillo tiene getTipoMenu()
        } else {
            // Modo Creación
            tituloLabel.setText("Añadir Nuevo Platillo");
            tipoMenuComboBox.getSelectionModel().selectFirst();
        }
    }

    /**
     * Maneja el evento de clic del botón "Guardar".
     * <p>Valida los campos, crea o actualiza el objeto {@code Platillo}, lo persiste
     * a través del servicio, refresca la vista principal y cierra el modal.</p>
     */
    @FXML
    private void guardar() {
        try {
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            // Intenta parsear el precio, capturando NumberFormatException si falla
            float precio = Float.parseFloat(precioField.getText());
            String tipoMenu = tipoMenuComboBox.getValue();

            if (nombre.isEmpty() || tipoMenu == null) {
                new Alert(Alert.AlertType.ERROR, "El nombre y el tipo de menú son obligatorios.").showAndWait();
                return;
            }

            if (platilloActual == null) {
                // Creación de nuevo platillo
                // Nota: Asumiendo que el constructor de Platillo acepta un parámetro para el tipo de menú.
                platilloActual = new Platillo(0, nombre, descripcion, precio, true, null, tipoMenu);
            } else {
                // Actualización del platillo existente
                platilloActual.setNombre(nombre);
                platilloActual.setDescripcion(descripcion);
                platilloActual.setPrecio(precio);
                platilloActual.setTipoMenu(tipoMenu); // Asumiendo que Platillo tiene setTipoMenu()
            }

            // Llama al servicio para guardar o actualizar
            servicioMenu.guardarPlatillo(platilloActual);

            controladorPrincipal.refrescarVista(); // Actualiza la tabla en el controlador principal
            cancelar(); // Cierra el modal

        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "El precio debe ser un número válido.").showAndWait();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage()).showAndWait();
        }
    }

    /**
     * Maneja el evento de clic del botón "Cancelar".
     * <p>Obtiene la etapa (Stage) actual del modal y la cierra sin guardar cambios.</p>
     */
    @FXML
    private void cancelar() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }
}